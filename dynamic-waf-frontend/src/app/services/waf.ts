import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WafService {
  // Putanja do tvog Spring Boot-a
  private baseUrl = 'http://localhost:8080/api/waf';

  constructor(private http: HttpClient) { }

  // 1. Povlačenje liste banovanih IP adresa
  getBannedIps(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/banned-ips`);
  }

  // 2. Simulacija napada (klikom na dugme)
  simulateAttack(ip: string): Observable<string> {
    // responseType: 'text' stavljamo jer nam Spring vraća običan String (ne JSON)
    return this.http.post(`${this.baseUrl}/simulate-attack?ip=${ip}`, null, { responseType: 'text' });
  }
  // 3. Slanje pojedinačnog sumnjivog zahteva (za testiranje SQLi i XSS)
  analyzeRequest(ip: string, payload: string): Observable<string> {
    return this.http.post(`${this.baseUrl}/analyze?ip=${ip}&payload=${payload}`, null, { responseType: 'text' });
  }

  // 4. Dinamičko dodavanje pravila (Templates)
  addDynamicRule(keyword: string, threatName: string): Observable<string> {
    return this.http.post(`${this.baseUrl}/add-rule?keyword=${keyword}&threatName=${threatName}`, null, { responseType: 'text' });
  }
}
