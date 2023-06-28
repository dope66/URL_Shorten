package com.project.UrlJrr.service;

import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.repository.ScrapRepository;
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

    public int calculateMatchingScore(User user, Long scrapId) {
        Scrap scrap = getScrapById(scrapId);
        if (scrap != null) {
            int score = calculateScore(user.getSkillStack(), scrap.getSkillStack());
            return score;
        } else {
            return 0;
        }
    }

    /*
    userSkillStack 을 ScrapSkillStack 에 하나씩 비교하면서
    점수를 올리는 형식
    매칭 점수 변경이 필요함.

    그래서 공고에 맞춰서 얼마나 맞는게 있는지 퍼센트로 확인,
    * */
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

    /*
    Jobtype enum의 이름과 비교
    * */
    public int getScoreFromJobType(String skill) {
        JobType[] jobTypes = JobType.values();
        for (JobType jobType : jobTypes) {
            if (jobType.getSkillName().equalsIgnoreCase(skill)) {
                return jobType.getScore();
            }
        }
        return 0;
    }

    /*
    Field enum의 이름과 비교
    * */
    public int getScoreFromField(String skill) {
        Field[] fields = Field.values();
        for (Field field : fields) {
            if (field.getSkillName().equalsIgnoreCase(skill)) {
                return field.getScore();
            }
        }
        return 0;
    }

    /*
    TechStack enum의 이름과 비교
    * */
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
