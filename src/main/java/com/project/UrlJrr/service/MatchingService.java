package com.project.UrlJrr.service;

import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.repository.ScrapRepository;
import com.project.UrlJrr.skillenum.JobType;
import com.project.UrlJrr.skillenum.TechStack;
import com.project.UrlJrr.skillenum.jobField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final ScrapRepository scrapRepository;

    public String calculateMatchingScore(User user, Long scrapId) {
        Scrap scrap = getScrapById(scrapId);
        String grade = null;
        if (scrap != null) {
            int score = calculateScore(user.getSkillStack(), scrap.getSkillStack());
            int experienceScore = calculateExperienceScore(user.getExperience(), scrap.getExperience());
            int sumScore = score + experienceScore;

            if (sumScore <=7) {
                grade = "D";
            } else if (sumScore <= 9) {
                grade = "C";
            } else if (sumScore <= 11) {
                grade = "B";
            } else if (sumScore <= 13) {
                grade = "A";
            } else {
                grade = "S";
            }
        }
        return grade;
    }

    public String translateExperience(String scrapExperience) {
        if (scrapExperience.contains("경력무관") || scrapExperience.contains("신입") || scrapExperience.contains("신입·")) {
            return "신입";
        } else if (scrapExperience.startsWith("경력")) {
            return "경력";
        } else {
            return null;
        }

    }

    public int calculateExperienceScore(String userExperience, String scrapExperience) {
        String scrapTranslatedExperience = translateExperience(scrapExperience);

        if (userExperience != null && userExperience.equals(scrapTranslatedExperience)) {
            // 매칭되는 경우
            return 5;
        } else {
            // 경력이 매칭되지 않는 경우
            return -5;
        }
    }

    public int calculateScore(String userSkillStack, String scrapSkillStack) {
        int totalScore = 0;

        String[] skills = scrapSkillStack.split(",");
        for (String skill : skills) {
            String trimmedSkill = skill.trim();
            System.out.println("skill: " + trimmedSkill);

            if (userSkillStack.contains(trimmedSkill)) {
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
        jobField[] fields = jobField.values();
        for (jobField field : fields) {
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
