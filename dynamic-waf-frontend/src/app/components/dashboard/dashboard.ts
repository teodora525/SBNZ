import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {WafService} from '../../services/waf';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class DashboardComponent implements OnInit {
  bannedIps: any[] = [];
  statusMessage: string = '';

  // Parametri za napad
  targetIp: string = '192.168.1.5';
  attackPayload: string = '';

  // Parametri za novo pravilo (Template)
  newRuleKeyword: string = 'UNION SELECT';
  newRuleThreat: string = 'SQL_INJECTION';

  constructor(private wafService: WafService) {}

  ngOnInit(): void {
    this.fetchBannedIps();
  }

  fetchBannedIps() {
    this.wafService.getBannedIps().subscribe({
      next: (data) => this.bannedIps = data,
      error: (err) => console.error(err)
    });
  }

  triggerAttack() {
    this.statusMessage = 'Šaljem sumnjiv zahtev...';
    this.wafService.analyzeRequest(this.targetIp, this.attackPayload).subscribe({
      next: (res) => {
        this.statusMessage = res;
        setTimeout(() => this.fetchBannedIps(), 500);
      },
      error: (err) => console.error(err)
    });
  }

  triggerBruteForce() {
    this.statusMessage = 'Simulacija Brute Force napada u toku...';
    this.wafService.simulateAttack(this.targetIp).subscribe({
      next: (res) => {
        this.statusMessage = res;
        setTimeout(() => this.fetchBannedIps(), 1000);
      },
      error: (err) => console.error(err)
    });
  }

  addNewRule() {
    this.statusMessage = 'Kreiram pravilo u letu...';
    this.wafService.addDynamicRule(this.newRuleKeyword, this.newRuleThreat).subscribe({
      next: (res) => {
        this.statusMessage = res;
      },
      error: (err) => console.error(err)
    });
  }
}
