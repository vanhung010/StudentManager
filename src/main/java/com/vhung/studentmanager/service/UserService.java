package com.vhung.studentmanager.service;

import com.vhung.studentmanager.repository.UserReposistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserReposistory userReposistory;

}
