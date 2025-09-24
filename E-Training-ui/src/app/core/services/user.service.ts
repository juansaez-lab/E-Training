import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import ConstUrls from "../../shared/contants/const-urls";
import { User } from 'src/app/interfaces/user';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) {}

    private END_POINT_URL = ConstUrls.API_URL + ConstUrls.CONTEXT_USER;

    findUsers(): Observable<HttpResponse<User[]>> {
        return this.http.get<User[]>(this.END_POINT_URL + '/', {

                    observe: 'response'
                });
    }

    findUser(id: number) {
         return this.http.get<User>(this.END_POINT_URL + '/' + id, {

                    observe: 'response'
                });
    }

    save(user: User) {
        return this.http.post<User>(this.END_POINT_URL + '/', user, {

                    observe: "response",
                });
    }  

    update(user: User) {
        return this.http.put<User>(this.END_POINT_URL + '/', user, {
                    //headers: headers,
                    observe: "response",
                });
    }

}
