package br.com.umc.cyclecare.daos;

import br.com.umc.cyclecare.models.DomainEntity;
import br.com.umc.cyclecare.models.User;
import br.com.umc.cyclecare.utils.HibernateUtil;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

import static br.com.umc.cyclecare.utils.Encrypt.encryptPassword;
import static br.com.umc.cyclecare.utils.Encrypt.matchPasswords;

@Repository
public class UserDao implements IDao {
    private final String DEFAULT_PASSWORD = "12345678";

    @Override
    public DomainEntity create(DomainEntity entity) {
        try {
            User user = (User) entity;

            validateUniqueFields(user);

            if (user.getPassword() != null) {
                String encryptedPassword = encryptPassword(user.getPassword());
                user.setPassword(encryptedPassword);
            }

            HibernateUtil.getSessionFactory().inTransaction(session -> {
                session.persist(user);
            });

            user.setPassword(null);
            return user;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public DomainEntity read(DomainEntity entity) {
        try {
            User user = (User) entity;
            HibernateUtil.getSessionFactory().inTransaction(session -> {
                User dbUser = session.get(User.class, user.getId());

                if (dbUser == null) {
                    throw new RuntimeException("Usuário não encontrado");
                }

                user.setId(dbUser.getId());
                user.setName(dbUser.getName());
                user.setCpf(dbUser.getCpf());
                user.setEmail(dbUser.getEmail());
                user.setPhone(dbUser.getPhone());
                user.setGoogleAccessToken(dbUser.getGoogleAccessToken());
            });
            return user;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public DomainEntity update(DomainEntity entity) {
        try {
            User user = (User) entity;
            HibernateUtil.getSessionFactory().inTransaction(session -> {
                if (user.getId() == null) {
                    String hql = "SELECT u FROM User u WHERE u.email = :email";
                    User dbUser = session.createQuery(hql, User.class)
                            .setParameter("email", user.getEmail())
                            .uniqueResult();

                    if (dbUser == null) {
                        return;
                    }

                    dbUser.setPassword(encryptPassword(DEFAULT_PASSWORD));
                    return;
                }

                User dbUser = session.get(User.class, user.getId());

                if (dbUser == null) {
                    throw new RuntimeException("Usuário não encontrado");
                }

                if (user.getCpf() != null) {
                    dbUser.setCpf(user.getCpf());
                }

                if (user.getPhone() != null) {
                    dbUser.setPhone(user.getPhone());
                }


                if (user.getCurrentPassword() != null) {
                    if (!matchPasswords(dbUser.getPassword(), user.getCurrentPassword())) {
                        throw new RuntimeException("Senha atual incorreta");
                    }
                    if (user.getPassword().matches(user.getCurrentPassword())) {
                        throw new RuntimeException("A nova senha deve ser diferente da senha atual");
                    }
                }

                if (user.getPassword() != null) {
                    String encryptedPassword = encryptPassword(user.getPassword());
                    dbUser.setPassword(encryptedPassword);
                }

                if (user.getGoogleAccessToken() != null) {
                    dbUser.setGoogleAccessToken(user.getGoogleAccessToken());
                }
            });

            user.setCurrentPassword(null);
            user.setPassword(null);
            return user;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(DomainEntity entity) {
        try {
            User user = (User) entity;
            HibernateUtil.getSessionFactory().inTransaction(session -> {
                User dbUser = session.get(User.class, user.getId());

                if (dbUser == null) {
                    throw new RuntimeException("Usuário não encotrado");
                }

                session.remove(dbUser);
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<DomainEntity> listAll(DomainEntity entity) {
        return List.of();
    }

    private void validateUniqueFields(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String email = user.getEmail();
            String phone = user.getPhone();
            String cpf = user.getCpf();

            String hql = "SELECT u FROM User u WHERE u.email = :email OR u.phone = :phone OR u.cpf = :cpf";
            User existingUser = session.createQuery(hql, User.class)
                    .setParameter("email", email)
                    .setParameter("phone", phone)
                    .setParameter("cpf", cpf)
                    .uniqueResult();

            if (existingUser != null) {
                if (existingUser.getEmail().equals(email)) {
                    throw new RuntimeException("Email já cadastrado");
                }
                if (existingUser.getPhone().equals(phone)) {
                    throw new RuntimeException("Telefone já cadastrado");
                }
                if (existingUser.getCpf().equals(cpf)) {
                    throw new RuntimeException("CPF já cadastrado");
                }
            }
        } finally {
            session.close();
        }
    }
}
