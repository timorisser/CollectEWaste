import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject, tap } from 'rxjs';
import { Address } from '../interfaces/address';

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  readonly URL: string = "http://localhost:8080/api/v1/account/location";

  addAddressObservable = new Subject<any>();

  constructor(private http: HttpClient) { }

  addLocation(address: Address): Observable<any> {
    const token = localStorage.getItem('token') || "none";
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers = headers.set('Authorization', 'Bearer ' + token);
    return this.http.post(`${this.URL}`, address, { 'headers': headers }).pipe(tap((o) => {
      this.addAddressObservable.next(o);
    }));
  }

  getLocation(): Observable<any> {
    const token = localStorage.getItem('token') || "none";
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers = headers.set('Authorization', 'Bearer ' + token);
    return this.http.get(`${this.URL}`, { 'headers': headers });
  }

  deleteLocation(id: number): Observable<any> {
    const token = localStorage.getItem('token') || "none";
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers = headers.set('Authorization', 'Bearer ' + token);
    return this.http.delete(`${this.URL}/${id}`, { 'headers': headers }).pipe(tap((o) => {
      this.addAddressObservable.next(o);
    }));
  }

  getOneLocation(id: number): Observable<any> {
    const token = localStorage.getItem('token') || "none";
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers = headers.set('Authorization', 'Bearer ' + token);
    return this.http.get(`${this.URL}/${id}`, { 'headers': headers });
  }
}
