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

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;

    public int calculateMatchingScore(User user, Long scrapId) {
        System.out.println("매칭 서비스 실행 ");
        Scrap scrap = getScrapById(scrapId);
        System.out.println("매칭 서비스에서 사용될 공고 ID =" + scrap);
        if (scrap != null) {
            System.out.println("스크랩이 비어있지않다.");
            int score = calculateScore(user.getSkillStack(), scrap.getSkillStack());
            System.out.println("유저가 보유한 skillstack" + user.getSkillStack());
            System.out.println("공고의 skillstack " + scrap.getSkillStack());
            System.out.println("매치 점수  Score: " + score);
            return score;
        } else {
            return 0;
        }
    }


    private int calculateScore(String userSkillStack, String scrapSkillStack) {
        int totalScore = 0;

        String[] skills = userSkillStack.split(",");
        System.out.println(",을 기준으로 나눈 skills = " + Arrays.toString(skills));
        for (String skill : skills) {
            String trimmedSkill = skill.trim();
            System.out.println("skill: " + trimmedSkill);

            if (scrapSkillStack.contains(trimmedSkill)) {
                System.out.println("스크랩 스킬 스택 중 skill이 포함됨? = " + scrapSkillStack);
                System.out.println("trimmedSkill = " + trimmedSkill);
                totalScore += getScoreFromJobType(trimmedSkill);
                totalScore += getScoreFromField(trimmedSkill);
                totalScore += getScoreFromTechStack(trimmedSkill);
            }
        }

        System.out.println("totalScore: " + totalScore);
        return totalScore;
    }
/*
*  TO DO LIST
*  로그를 찍어보면 알겠지만,
* 처음 유저 스킬을 scrap skill에 대입해보고 있으면 1점을 추가하는건 됨,
* 그러나 그다음 2번째 유저 스킬을 찾는거 부터 안된다.
* getScoreFromJobType의 문제가 맞는거 같은데 왜 안되는지 이유를 잘모르겠음
* */
    private int getScoreFromJobType(String skill) {
        JobType[] jobTypes = JobType.values();
        for (JobType jobType : jobTypes) {
            if (jobType.name().equals(skill.toUpperCase())) {
                System.out.println("잡 타입 이름중 skill이랑 같은게 있는지 "+skill);
                System.out.println("jobType = " + jobType);
                System.out.println("잡타입 점수 ="+jobType.getScore());

                return jobType.getScore();
            }
        }
        return 0;
    }

    private int getScoreFromField(String skill) {
        Field[] fields = Field.values();
        for (Field field : fields) {
            if (field.name().equalsIgnoreCase(skill)) {
                System.out.println("field 타입 이름중 skill이랑 같은게 있는지 "+skill);
                System.out.println("field = " + field);
                System.out.println("field 점수 ="+field.getScore());
                return field.getScore();
            }
        }
        return 0;
    }

    private int getScoreFromTechStack(String skill) {
        TechStack[] techStacks = TechStack.values();
        for (TechStack techStack : techStacks) {
            if (techStack.name().equalsIgnoreCase(skill)) {
                System.out.println("techStack 타입 이름중 skill이랑 같은게 있는지 "+skill);
                System.out.println("techStack = " + techStack);
                System.out.println("techStack 점수 ="+techStack.getScore());
                return techStack.getScore();
            }
        }
        return 0;
    }

    private Scrap getScrapById(Long scrapId) {
        Optional<Scrap> scrapOptional = scrapRepository.findById(scrapId);
        return scrapOptional.orElse(null);
    }

}
