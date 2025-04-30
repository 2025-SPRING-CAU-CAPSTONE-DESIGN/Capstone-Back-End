package com.capstone.storyforest.wordgame.config;

import com.capstone.storyforest.wordgame.entity.Word;
import com.capstone.storyforest.wordgame.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WordDataLoader implements CommandLineRunner {

    private final WordRepository wordRepository;

    @Override
    public void run(String... args) throws Exception {
        if(wordRepository.count() >0) return;

        try(Workbook workbook = new XSSFWorkbook(
                getClass().getResourceAsStream("/단어.xlsx")
        )) {
            Sheet sheet=workbook.getSheetAt(0);

            for(int i =1;i<=sheet.getLastRowNum();i++){
                Row row= sheet.getRow(i);
                if(row==null) continue;

                Word word=new Word();
                word.setTerm(row.getCell(0).getStringCellValue());
                word.setMeaning(row.getCell(1).getStringCellValue());
                word.setDifficulty((int) row.getCell(2).getNumericCellValue());

                wordRepository.save(word);
            }

            System.out.println("WordDataLoader | loaded rows: "+wordRepository.count());
        }
    }
}
