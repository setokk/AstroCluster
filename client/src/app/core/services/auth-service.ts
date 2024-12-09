import {HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Constants} from "./constants";

@Injectable({providedIn: 'root'})
export class AuthService {
    constructor() {}

    getHttpHeaders(): HttpHeaders {
        const jwtToken: any = localStorage.getItem(Constants.JWT_TOKEN_STORAGE_KEY);
        if (jwtToken === null) {
            return new HttpHeaders();
        } else {
            return  new HttpHeaders({
                'Authorization': `Bearer ${jwtToken}`
            });
        }
    }

    isPublicUser(): boolean {
        return localStorage.getItem(Constants.JWT_TOKEN_STORAGE_KEY) === null;
    }
}