# Authentication Service Specification

## Overview

The Authentication Service is a critical component of the system, responsible for managing user registration, login, and token management. It provides a secure way for users to access the system by validating their credentials and issuing JWT tokens for authenticated sessions.

## Features

### User Registration

Endpoint: `POST /api/v1/auth/register`

This feature allows new users to register for an account. The user provides their details (such as username, password, email, etc.) and the service creates a new user account in the system. The password is hashed using bcrypt for secure storage.

### User Login

Endpoint: `POST /api/v1/auth/authenticate`

This feature allows users to log in to their account. The user provides their username and password, and the service validates these credentials. If the credentials are valid, the service generates a JWT token for the user's session and returns it in the response.

### Reset Password (To be implemented)

This feature will allow users to reset their password if they forget it. The user will provide their email, and the service will send a password reset link to that email. The user can then use this link to set a new password for their account.

### Refresh Token (To be implemented)

This feature will allow users to refresh their session token. The user will provide their current token, and the service will validate this token and issue a new one. This allows the user to maintain their session without needing to log in again.

## Security

The service uses JWT for session management. When a user logs in, they are issued a JWT token. This token is then used to authenticate the user's requests. The service validates the token with each request to ensure that it is valid and has not expired.

Passwords are hashed using bcrypt before they are stored. This ensures that even if the user data is compromised, the passwords cannot be easily recovered.

## API Documentation

The API documentation for the service is available at `http://localhost:8080/swagger-ui/index.html`. This provides detailed information about the API endpoints, including the request and response formats.

## Next version

The next version of the service will include the following features:

- Reset Password
- Refresh Token
- Login by Email and dynamic code
- Improve security by register and reset password by email

