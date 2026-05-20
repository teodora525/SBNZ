# Ekspertski Sistem za Detekciju i Prevenciju Web Napada (Dinamički WAF)

[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-%236DB33F?logo=springboot)](https://spring.io/projects/spring-boot)
[![Drools](https://img.shields.io/badge/Drools-8.x-red.svg)](https://www.drools.org/)
[![Angular](https://img.shields.io/badge/Angular-17+-dd0031?logo=angular)](https://angular.io/)
[![Nginx](https://img.shields.io/badge/Nginx-reverse--proxy-green.svg)](https://www.nginx.com/)
[![MIT License](https://img.shields.io/github/license/teodora525/SBNZ)](./LICENSE)

---

## 🛡️ Opis projekta

Ovaj projekat je **ekspertski sistem za detekciju i prevenciju web napada** (Dinamički WAF) razvijen u sklopu predmeta "Sistemi bazirani na znanju". Cilj je real-time analiza HTTP zahteva na osnovu fleksibilnih pravila, konteksta i agregacije događaja, kako bi se web aplikacije efikasno zaštitile od savremenih napada kao što su **SQL Injection, XSS, Path Traversal, Brute Force**, i skeniranje endpoint-a.

Projekat koristi domenski specifičan jezik (DSL) za lako definisanje pravila, temeljno agregira događaje kroz vreme (CEP), automatski menja bezbednosni kontekst i podržava proširivu bazu znanja nad Drools engine-om.

---

## 🚀 Tehnologije

- **Backend:** Java 17+, Spring Boot 3.x, Drools 8.x (rules engine)
- **Frontend:** Angular 17+ (dashboard, administracija)
- **Reverse Proxy:** Nginx
- **Baza podataka:** PostgreSQL ili drugi RDBMS (za skladište konfiguracija, događaja i izveštaja)
- **Komunikacija:** REST API
- **DevOps:** Docker (opciono za deployment), Maven

---

## 🏷️ Ključne funkcionalnosti

- Real-time analiza i procesiranje HTTP zahteva
- Detekcija SQL injection, XSS, path traversal i drugih tipova napada
- Prepoznavanje i agregacija incidenata pomoću CEP (Complex Event Processing)
- Fleksibilni režimi zaštite (konteksti): Normal_Traffic, Under_Attack, Maintenance, Zero_Trust
- Dinamička promena konteksta na osnovu pravila i broja incidenata
- Odluke: Allow, Drop, Ban, Alert, Log
- Administratorski dashboard sa prikazom incidenata, blokiranih IP adresa, izveštaja, notifikacija
- Definisanje i proširivanje pravila kroz user-friendly DSL i Drools templates

---

## 🛠️ Arhitektura

**Višeslojna arhitektura:**
1. **Nginx** — reverse proxy, prosleđuje detalje zahteva WAF servisu
2. **Spring Boot WAF servis** — transformiše zahteve u događaje i prosleđuje Drools engine-u, upravlja konfiguracijama i incidentima
3. **Drools Engine** — izvršava pravila nad događajima i menja kontekst sistema po potrebi
4. **Baza podataka** — čuva konfiguracije, pravila, incidente, izveštaje
5. **Angular frontend** — dashboard za praćenje stanja, administraciju i analitičke izveštaje

---

## 📊 Izveštaji & dashboard

- Izveštaji o uzrocima incidenata (root-cause)
- Liste blokiranih IP adresa i endpointa
- Statistika napada (tipovi napada, najaktivniji napadači, periodične analize)
- Prikaz promena konteksta i razloga za promenu
- Prikaz događaja uživo (live feed, notifikacije)

---

## 🧩 Primer DLS pravila (domenski jezik za kreiranje pravila)

```dsl
when
    query contains "UNION SELECT"
    or query contains "' OR '1'='1"
then
    block request
    and log as SQL_INJECTION_ATTEMPT
    and increment threat score
```

Više primera i uputstva možeš naći u dokumentaciji projekta.

---

## ▶️ Pokretanje projekta (lokalno)

1. **Kloniraj repozitorijum**
   ```bash
   git clone https://github.com/teodora525/SBNZ.git
   cd SBNZ
   ```

2. **Backend:**  
   - Instaliraj Java 17+ i Maven
   - `cd backend`
   - `mvn clean package`
   - Pokreni aplikaciju: `java -jar target/<artifact>.jar`

3. **Frontend:**
   - Instaliraj Node.js i Angular CLI
   - `cd frontend`
   - `npm install`
   - `ng serve`

4. **Nginx i baza podataka** (opcioni, konfiguracija u `/deploy`)

---

## 📖 Developed:

- Teodora Nikolić,https://github.com/teodora525

---

## 📄 Licence

Distribuira se pod [MIT licencom](./LICENSE).

---

## Credits

Ovaj projekat je realizovan u okviru predmeta **Sistemi bazirani na znanju** na Fakultetu tehničkih nauka, smer Softversko inženjerstvo i informacione tehnologije.
