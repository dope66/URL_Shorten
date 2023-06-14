package com.project.UrlJrr.service;

import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.repository.ScrapRepository;
import com.project.UrlJrr.repository.UserRepository;
import com.project.UrlJrr.skillenum.Field;
import com.project.UrlJrr.skillenum.JobType;
import com.project.UrlJrr.skillenum.TechStack;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;

    public int calculateMatchingScore(User user, Long scrapId) {
        Scrap scrap = getScrapById(scrapId);
        if (scrap != null) {
            int score = calculateScore(user.getSkillStack(), scrap.getSkillStack());
            System.out.println("Matching Score: " + score);
            return score;
        }
        return 0;
    }

    private Scrap getScrapById(Long scrapId) {
        Optional<Scrap> scrapOptional = scrapRepository.findById(scrapId);
        return scrapOptional.orElse(null);
    }

    private int calculateScore(String userSkillStack, String scrapSkillStack) {
        int totalScore = 0;

        for (String skill : userSkillStack.split(",")) {
            totalScore += getScoreFromSkill(skill.trim(), scrapSkillStack);
        }

        return totalScore;
    }

    private int getScoreFromSkill(String skill, String scrapSkillStack) {
        int score = 0;

        if (scrapSkillStack.contains(skill)) {
            score += getScoreFromJobType(skill);
            System.out.println("jobtype+"+score);
            score += getScoreFromField(skill);
            System.out.println("Field+"+score);
            score += getScoreFromTechStack(skill);
            System.out.println("TechStack+"+score);
        }
        System.out.println(score);
        return score;
    }

    private int getScoreFromJobType(String skill) {
        JobType[] jobTypes = JobType.values();
        for (JobType jobType : jobTypes) {
            if (jobType.name().equalsIgnoreCase(skill)) {
                return jobType.getScore();
            }
        }
        return 0; // 기본값: 0점
    }

    private int getScoreFromField(String skill) {
        Field[] fields = Field.values();
        for (Field field : fields) {
            if (field.name().equalsIgnoreCase(skill)) {
                return field.getScore();
            }
        }
        return 0;
    }

    private int getScoreFromTechStack(String skill) {
        TechStack[] techStacks = TechStack.values();
        for (TechStack techStack : techStacks) {
            if (techStack.name().equalsIgnoreCase(skill)) {
                return techStack.getScore();
            }
        }
        return 0;
    }
}
