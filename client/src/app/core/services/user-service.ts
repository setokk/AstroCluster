import { HttpClient } from "@angular/common/http";
import { Api } from "./api";
import { Injectable } from "@angular/core";
import {Observable, tap} from "rxjs";
import {RegisterUserRequest} from "../request/RegisterUserRequest";
import {LoginUserRequest} from "../request/LoginUserRequest";
import {Constants} from "./constants";

@Injectable({providedIn: 'root'})
export class UserService {
    constructor(private http: HttpClient) {}

    registerUser(registerUserRequest: RegisterUserRequest): Observable<string> {
        return this.http.post<string>(Api.USERS_REGISTER_ENDPOINT, registerUserRequest).pipe(
            tap(token => localStorage.setItem(Constants.JWT_TOKEN_STORAGE_KEY, token))
        );
    }

    loginUser(loginUserRequest: LoginUserRequest): Observable<string> {
        return this.http.post<string>(Api.USERS_LOGIN_ENDPOINT, loginUserRequest).pipe(
            tap(token => localStorage.setItem(Constants.JWT_TOKEN_STORAGE_KEY, token))
        );
    }
}