package com.capstone.storyforest.wordgame.config;

import java.time.LocalDate;
import java.time.Period;

public class LevelUtil {
    /**
     * 생년월일(birthDate)을 기반으로 학교 학년을 계산하고, 그 학년에 따라 레벨(1~3)을 반환합니다.
     * 한국 학제 기준:
     *  - grade = 현재 연도 - 출생 연도 - 6
     *  - grade < 1   => 레벨 1 (초등 1~2학년 이하)
     *  - grade 1~2   => 레벨 1
     *  - grade 3~4   => 레벨 2 (초등 3~4학년)
     *  - grade >= 5  => 레벨 3 (초등 5~6학년)
     */
    public static int calculateLevel(LocalDate birthDate){
        int currentYear = LocalDate.now().getYear();
        int grade=currentYear-birthDate.getYear()-6; // 학년 계산
        if(grade>=5){
            return 3;
        }
        else if(grade>=3){
            return 2;
        }
        else{
            return 1;
        }
    }

}
