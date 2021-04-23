package com.pairlearning.expensetracker.domain;

public class User {

    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private User(UserBuilder userBuilder) {
        this.userId = userBuilder.userId;
        this.firstName = userBuilder.firstName;
        this.lastName = userBuilder.lastName;
        this.email = userBuilder.email;
        this.password = userBuilder.password;
    }

    public static class UserBuilder {

        private Integer userId;
        private String firstName;
        private String lastName;
        private String email;
        private String password;

        public UserBuilder(Integer userId, String firstName, String lastName, String email, String password) {
            this.userId = userId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
        }

        public User build() {
            return new User(this);
        }
    }

    public Integer getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
