package com.group.libraryapp.service.user;

import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.JavaUserRepository;
import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JavaUserService {

  private final JavaUserRepository userRepository;

  public JavaUserService(JavaUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public void saveUser(UserCreateRequest request) {
    User newUser = new User(request.getName(), request.getAge(), Collections.emptyList(),null);
    userRepository.save(newUser);
  }

  @Transactional(readOnly = true)
  public List<UserResponse> getUsers() {
    // 자바코드로 만든 Dto가 없어지면서 좀 어그러졌음
//    return userRepository.findAll().stream()
//        .map(UserResponse::new)
//        .collect(Collectors.toList());
    return null;
  }

  @Transactional
  public void updateUserName(UserUpdateRequest request) {
    User user = userRepository.findById(request.getId()).orElseThrow(IllegalArgumentException::new);
    user.updateName(request.getName());
  }

  @Transactional
  public void deleteUser(String name) {
    User user = userRepository.findByName(name).orElseThrow(IllegalArgumentException::new);
    userRepository.delete(user);
  }

}
