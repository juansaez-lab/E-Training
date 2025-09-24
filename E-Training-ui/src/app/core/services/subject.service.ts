import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import ConstUrls from "../../shared/contants/const-urls";
import { User } from 'src/app/interfaces/user';
import { Observable } from 'rxjs';
import { Subject } from 'src/app/interfaces/subject';


@Injectable({
  providedIn: 'root'
})
export class SubjectService {
  constructor(private http: HttpClient) {}

    private END_POINT_URL = ConstUrls.API_URL + ConstUrls.CONTEXT_SUBJECT;

    findAll(): Observable<HttpResponse<Subject[]>> {
        return this.http.get<Subject[]>(this.END_POINT_URL + '/', {
                    //headers: headers,
                    observe: 'response'
                });
    }

    find(id: number) {
         return this.http.get<Subject>(this.END_POINT_URL + '/' + id, {
                    //headers: headers,
                    observe: 'response'
                });
    }

    save(subject: Subject) {
        return this.http.post<Subject>(this.END_POINT_URL + '/', subject, {
                    //headers: headers,
                    observe: "response",
                });
    }  

    update(subject: Subject) {
        return this.http.put<Subject>(this.END_POINT_URL + '/', subject, {
                    //headers: headers,
                    observe: "response",
                });
    }

}
