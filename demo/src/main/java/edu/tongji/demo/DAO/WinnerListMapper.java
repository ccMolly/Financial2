package edu.tongji.demo.dao;

import edu.tongji.demo.model.WinnerList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface WinnerListMapper {

    @Select("select * from winnerlist order by reason")
    ArrayList<WinnerList> getAll();
}
