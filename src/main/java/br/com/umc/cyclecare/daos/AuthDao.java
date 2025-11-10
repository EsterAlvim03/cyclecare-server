package br.com.umc.cyclecare.daos;

import br.com.umc.cyclecare.dtos.AuthRequest;
import br.com.umc.cyclecare.dtos.GoogleUserInfo;
import br.com.umc.cyclecare.models.User;
import br.com.umc.cyclecare.utils.HibernateUtil;
import br.com.umc.cyclecare.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthDao {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDao userDao;

    public String login(AuthRequest auth) {
        try {
            User user = findUserByEmail(auth.getEmail());

            if (user == null) {
                throw new RuntimeException("Email ou senha inválidos");
            }

            if (!passwordEncoder.matches(auth.getPassword(), user.getPassword())) {
                throw new RuntimeException("Email ou senha inválidos");
            }


            return JwtUtil.generateToken(user.getId());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String googleLogin(GoogleUserInfo googleUserInfo, String accessToken) {
        try {
            User user = findUserByEmail(googleUserInfo.getEmail());

            if (user == null) {
                User newUser = new User();
                newUser.setEmail(googleUserInfo.getEmail());
                newUser.setName(googleUserInfo.getName());
                newUser.setGoogleAccessToken(accessToken);

                User respUser = (User) userDao.create(newUser);
                return JwtUtil.generateToken(respUser.getId());
            }

            if (user.getGoogleAccessToken() == null) {
                user.setGoogleAccessToken(accessToken);
                userDao.update(user);
            }

            return JwtUtil.generateToken(user.getId());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private User findUserByEmail(String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "FROM User u WHERE u.email = :email";
            return session.createQuery(hql, User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }
}