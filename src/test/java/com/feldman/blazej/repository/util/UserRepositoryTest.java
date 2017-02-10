package com.feldman.blazej.repository.util;

import com.feldman.blazej.BaseSpringDatabseConfig;
import com.feldman.blazej.model.User;
import com.feldman.blazej.repository.RepositoryCrudTest;
import com.feldman.blazej.repository.TestDataProviderUtil;
import com.feldman.blazej.repository.UserRepository;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserRepositoryTest extends BaseSpringDatabseConfig implements RepositoryCrudTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() throws Exception {

        user = TestDataProviderUtil.newTestUser();
        assertNotNull(userRepository);
        assertNotNull(user);
    }

    @Test
    @Override
    public void a_insertTest() {
        User save = userRepository.save(user);
        assertNotNull(save.getUserId());

        User userRepositoryOne = userRepository.findOne(user.getUserId());
        assertUser(userRepositoryOne);
    }

    @Test
    @Override
    public void b_readTest() {
        List<User> all = userRepository.findAll();

        assertNotNull(all);
        assertFalse(all.isEmpty());
    }

    @Test
    @Override
    public void c_updateTest() {
        List<User> all = userRepository.findAll();
        // all.size() - 1 - pobieram ostatni z dodanych użytkowników
        User userToUpdate = all.get(all.size() - 1);
        assertNotNull(userToUpdate);
        assertUser(userToUpdate);

        userToUpdate.setEmail("new@new.pl");
        userToUpdate.setPassword("!@#$%^&*9");
        userRepository.save(userToUpdate);

        User updatedUser = userRepository.findOne(userToUpdate.getUserId());
        assertEquals(updatedUser.getEmail(), "new@new.pl");
        assertEquals(updatedUser.getPassword(), "!@#$%^&*9");
    }

    @Test
    @Override
    public void d_deleteTest() {
        List<User> all = userRepository.findAll();
        User userToDelete = all.get(0);
        assertNotNull(userToDelete);

        userRepository.delete(userToDelete);
        User deletedUser = userRepository.findOne(userToDelete.getUserId());
        assertNull(deletedUser);
    }

    private void assertUser(User user) {
        assertEquals(user.getName(), "Name");
        assertEquals(user.getSurname(), "SureName");
        assertEquals(user.getEmail(), "test@test.pl");
        assertEquals(user.getUserLogin(), "login");
        assertEquals(user.getPassword(), "password");
    }
}