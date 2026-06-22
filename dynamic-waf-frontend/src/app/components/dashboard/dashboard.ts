import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WafService } from '../../services/waf';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule], // Uvozimo module neophodne za rad sa formama i listama
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class DashboardComponent implements OnInit {
  bannedIps: any[] = [];
  targetIp: string = '10.0.0.99'; // Neka default IP adresa za testiranje
  statusMessage: string = '';

  constructor(private wafService: WafService) {}

  // Ova metoda se poziva automatski čim se stranica učita
  ngOnInit(): void {
    this.fetchBannedIps();
  }

  // Funkcija za povlačenje podataka
  fetchBannedIps() {
    this.wafService.getBannedIps().subscribe({
      next: (data) => {
        this.bannedIps = data;
      },
      error: (err) => console.error('Greška pri učitavanju adresa:', err)
    });
  }

  // Funkcija koja se okida kada klikneš na dugme
  triggerAttack() {
    this.statusMessage = 'Simulacija u toku, ispaljujem zahteve...';

    this.wafService.simulateAttack(this.targetIp).subscribe({
      next: (res) => {
        this.statusMessage = res;
        // Čekamo pola sekunde da Drools odradi svoje i onda osvežavamo listu na ekranu
        setTimeout(() => this.fetchBannedIps(), 500);
      },
      error: (err) => {
        this.statusMessage = 'Greška pri simulaciji!';
        console.error(err);
      }
    });
  }
}
