package edu.tongji.demo.ServiceImpl;

import edu.tongji.demo.DAO.UserInfoMapper;
import edu.tongji.demo.Model.UserInfo;
import edu.tongji.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer getIDByName(String name){
        try{
            if (name.equals(""))
                return -1;
            return userInfoMapper.getID(name);
        } catch (Exception e){
            return -2;
        }
    }

    @Override
    public String getNameByCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String name = "";
        for(int i = 0; i < cookies.length; i++){
            if(cookies[i].getName().equals("fnan")){
                name = cookies[i].getValue();
                break;
            }
        }
        return name;
    }

    @Override
    public void addSession(String name, String password, HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        session.setAttribute("name", name);
        session.setAttribute("password", password);
        session.setMaxInactiveInterval(60*60);

        Cookie cookie = new Cookie("fnan", name);
        cookie.setPath("/");
        cookie.setMaxAge(60*60);
        response.addCookie(cookie);
    }

    @Override
    public boolean verify(String name, String password){
        try{
            if (userInfoMapper.Vefify(name, password) == null)
                return false;
            else
                return true;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++){
            if(cookies[i].getName().equals("fnan")){
                Cookie cookie = new Cookie("fnan", "ww");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    public boolean signUp(String name, String password, String email, String nickname){
        try{
            SqlRowSet set = jdbcTemplate.queryForRowSet("SELECT name FROM user_info WHERE name = \'" + name + "\';");
            Boolean judge = true;
            if (set.next()){
                judge = false;
            }
            if (judge == true) {
                String sql = "INSERT INTO user_info(password, name, email, nickname) VALUES (\'"+
                        password + "\',\'" + name + "\', \'"+ email + "\', \'" + nickname +"\');";
                System.out.println(sql);
                jdbcTemplate.execute(sql);
            }
            return judge;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public UserInfo getUserInformation(HttpServletRequest request){
        try{
            String name = getNameByCookie(request);
            System.out.println(name);
            return userInfoMapper.getInformationByName(name);
        }catch (Exception e){
            return null;
        }
    }
}
