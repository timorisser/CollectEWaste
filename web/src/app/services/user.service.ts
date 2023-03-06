import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject, tap } from 'rxjs';
import { User } from '../interfaces/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  readonly URL: string = "http://localhost:8080/api/v1/account";

  userChangedObservable = new Subject<any>();

  user: User = {};
  is_company: string | null = localStorage.getItem('is_company');

  constructor(private http: HttpClient,) { }

  getUser(): Observable<any> {
    const id = localStorage.getItem('id') || "none";
    const token = localStorage.getItem('token') || "none";
    this.is_company = localStorage.getItem('is_company');
    console.log(id);

    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers = headers.set('Authorization', 'Bearer ' + token);
    console.log(headers);
    return this.http.get(`${this.URL}/`, { 'headers': headers });
  }

  getUserWithId(id: number): Observable<any> {
    const token = localStorage.getItem('token') || "none";
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers = headers.set('Authorization', 'Bearer ' + token);
    return this.http.get(`${this.URL}/${id}`, { 'headers': headers });

  }

  changePassword(oldpassword: string, newpassword: string, matchingpassword: string): Observable<any> {
    const token = localStorage.getItem('token') || "none";
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    headers = headers.set('Authorization', 'Bearer ' + token);
    return this.http.patch(`${this.URL}/changePassword`, { 'newpassword': newpassword, 'matchingpassword': matchingpassword, 'oldpassword': oldpassword }, { 'headers': headers });
  }

  updateFirstnameLastnamePhonenumber(firstname: string, lastname: string, phonenumber: string): Observable<any> {
    const token = localStorage.getItem('token') || "none";
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers = headers.set('Authorization', 'Bearer ' + token);
    return this.http.patch(`${this.URL}`, { 'firstname': firstname, 'lastname': lastname, 'phonenumber': phonenumber }, { 'headers': headers }).pipe(tap((o) => {
      this.userChangedObservable.next(o);
    }))
  }

  deleteUser(): Observable<any> {
    const token = localStorage.getItem('token') || "none";
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers = headers.set('Authorization', 'Bearer ' + token);
    return this.http.delete(`${this.URL}`, { 'headers': headers });
  }

  // getUserObj() {
  //   this.getUser().subscribe((data) => {
  //     console.log(data);

  //     this.user = data;
  //   });
  // }

  upload(formData: FormData): Observable<any> {
    return this.http.post('http://localhost:1109/upload', formData);
  }

  getProfileImg(uid: number): Observable<any> {
    const user_id = { uid: uid };
    console.log(user_id);

    return this.http.post(`http://localhost:1109/profileImg`, user_id, { responseType: 'text' });
  }

}
