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
            return score;
        } else {
            return 0;
        }
    }


    public int calculateScore(String userSkillStack, String scrapSkillStack) {
        int totalScore = 0;

        String[] skills = userSkillStack.split(",");
        for (String skill : skills) {
            String trimmedSkill = skill.trim();
            System.out.println("skill: " + trimmedSkill);

            if (scrapSkillStack.contains(trimmedSkill)) {
                totalScore += getScoreFromJobType(trimmedSkill);
                totalScore += getScoreFromField(trimmedSkill);
                totalScore += getScoreFromTechStack(trimmedSkill);
            }
        }

        System.out.println("totalScore: " + totalScore);
        return totalScore;
    }


    public int getScoreFromJobType(String skill) {
        JobType[] jobTypes = JobType.values();
        for (JobType jobType : jobTypes) {
            if (jobType.getSkillName().equalsIgnoreCase(skill)) {
                return jobType.getScore();
            }
        }
        return 0;
    }

    public int getScoreFromField(String skill) {
        Field[] fields = Field.values();
        for (Field field : fields) {
            if (field.getSkillName().equalsIgnoreCase(skill)) {
                return field.getScore();
            }
        }
        return 0;
    }

    public int getScoreFromTechStack(String skill) {
        TechStack[] techStacks = TechStack.values();
        for (TechStack techStack : techStacks) {
            if (techStack.getSkillName().equalsIgnoreCase(skill)) {
                return techStack.getScore();
            }
        }
        return 0;
    }

    public Scrap getScrapById(Long scrapId) {
        Optional<Scrap> scrapOptional = scrapRepository.findById(scrapId);
        return scrapOptional.orElse(null);
    }

}
