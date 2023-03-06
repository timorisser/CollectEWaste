import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject, tap } from 'rxjs';
import { Appointment } from '../interfaces/appointment';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  readonly URL: string = "http://localhost:8080/api/v1/account/appointment";

  addAppointmentObservable = new Subject<any>();

  days: string[] = [
    "Montag",
    "Dienstag",
    "Mittwoch",
    "Donnerstag",
    "Freitag",
    "Samstag",
    "Sonntag"
  ];

  constructor(private http: HttpClient) { }

  addAppointment(appointment: Appointment): Observable<any> {
    console.log("APP:" + appointment.day + " " + appointment.locationid + " " + appointment.startTime + " " + appointment.endTime);

    const token = localStorage.getItem('token') || "none";
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers = headers.set('Authorization', 'Bearer ' + token);
    return this.http.post(`${this.URL}`, appointment, { 'headers': headers }).pipe(tap((o) => {
      this.addAppointmentObservable.next(o);
    }));
  }

  deleteAppointment(id: number): Observable<any> {
    const token = localStorage.getItem('token') || "none";
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers = headers.set('Authorization', 'Bearer ' + token);
    return this.http.delete(`${this.URL}/${id}`, { 'headers': headers, responseType: 'text' });
  }

  getAppointmentsFromLocation(id: number): Observable<any> {
    const token = localStorage.getItem('token') || "none";
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    headers = headers.set('Authorization', 'Bearer ' + token);
    return this.http.get(`${this.URL}/loc/${id}`, { 'headers': headers });
  }
}
