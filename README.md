
# Ekspertski Sistem za Detekciju i Prevenciju Web Napada (Dinamički WAF)

[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-%236DB33F?logo=springboot)](https://spring.io/projects/spring-boot)
[![Drools](https://img.shields.io/badge/Drools-8.x-red.svg)](https://www.drools.org/)
[![Angular](https://img.shields.io/badge/Angular-17+-dd0031?logo=angular)](https://angular.io/)
[![Nginx](https://img.shields.io/badge/Nginx-reverse--proxy-green.svg)](https://www.nginx.com/)
[![MIT License](https://img.shields.io/github/license/teodora525/SBNZ)](./LICENSE)

---

## 🛡️ Opis projekta

Ovaj projekat je **ekspertski sistem za detekciju i prevenciju web napada** (Dinamički WAF) razvijen u sklopu predmeta "Sistemi bazirani na znanju". Cilj je *real-time* analiza HTTP zahteva na osnovu Drools pravila u cilju blokiranja različitih bezbednosnih pretnji.

Projekat koristi domenski specifičan jezik (DSL) za lako definisanje pravila, temeljno agregira događaje kroz vreme koristeći CEP (*Complex Event Processing*), automatski menja bezbednosni kontekst na osnovu dešavanja i podržava bazu znanja koja se može lako proširiti.

---

## 🚀 Tehnologije

- **Backend:** Java 17+, Spring Boot 3.x, Drools 8.x (Rules Engine)
- **Frontend:** Angular 17+ (Dashboard, Administracija)
- **Reverse Proxy:** Nginx
- **Baza podataka:** PostgreSQL (ili drugi RDBMS za skladište konfiguracija, događaja i izveštaja)
- **Komunikacija:** REST API
- **DevOps:** Docker (opciono za deployment), Maven

---

## 🏷️ Ključne funkcionalnosti

- Real-time analiza i procesiranje HTTP zahteva
- Detekcija tipičnih napada kao što su **SQL Injection**, **XSS**, **Path Traversal** i drugi
- Prepoznavanje i agregacija incidenata pomoću CEP tehnika
- Fleksibilni režimi zaštite (konteksti rada): `Normal_Traffic`, `Under_Attack`, `Maintenance`, `Zero_Trust`
- Dinamička promena konteksta na osnovu okidanja pravila i broja incidenata
- Moguće akcije: `Allow`, `Drop`, `Ban`, `Alert`, `Log`
- Administratorski dashboard za upravljanje: prikaz incidenata, blokiranih IP adresa, izveštaja, i *live* notifikacija
- Definisanje i proširivanje pravila kroz razumljiv *user-friendly* DSL i Drools templates

---

## 🛠️ Arhitektura sistema

**Višeslojna arhitektura obuhvata:**
1. **Nginx** — Reverse proxy koji prikuplja i prosleđuje detalje zahteva ka WAF servisu.
2. **Spring Boot WAF servis** — Prima zahteve, transformiše ih u događaje, upravlja konfiguracijama incidenata i komunicira sa engine-om.
3. **Drools Engine** — Jezgro sistema; procesira događaje i donosi odluke prema definisanim pravilima i po potrebi menja kontekst sistema.
4. **Baza podataka** — Perzistentno čuva konfiguracije, pravila, istoriju incidenata i podatke za izveštaje.
5. **Angular frontend** — Klijentski panel namenjen za praćenje stanja, laku administraciju i uvid u analitičke izveštaje.

---

## 📊 Izveštaji & Dashboard

- **Izveštaji o uzrocima incidenata** (Root-cause analiza)
- Pregledne **liste blokiranih IP adresa** i ciljanih endpoint-a
- **Statistika napada:** najčešći tipovi napada, najaktivniji napadači, periodične vremenske analize
- Vizuelni prikaz promena bezbednosnog konteksta i razloga za iste
- *Live feed* i notifikacije o novim događajima na platformi

---

## 🧩 Primer DSL pravila (Domenski jezik)

Umesto pisanja kompleksnog koda, definisanje pravila izgleda ovako:

```dsl
when
    query contains "UNION SELECT"
    or query contains "' OR '1'='1"
then
    block request
    and log as SQL_INJECTION_ATTEMPT
    and increment threat score
```

> *Više primera i uputstava možete naći u dokumentaciji samog projekta.*

---

## ▶️ Pokretanje projekta (Lokalno)

1. **Klonirajte repozitorijum**
   ```bash
   git clone https://github.com/teodora525/SBNZ.git
   cd SBNZ
   ```

2. **Backend (Spring Boot):**  
   - Proverite da li imate instaliranu Javu 17+ i Maven.
   - Pozicionirajte se u folder backenda:
     ```bash
     cd backend
     mvn clean package
     java -jar target/sbnz-backend-0.0.1-SNAPSHOT.jar
     ```
   *(Napomena: Prilagodite naziv `.jar` fajla u zavisnosti od imena u `pom.xml`).*

3. **Frontend (Angular):**
   - Proverite da li imate instaliran Node.js i Angular CLI.
   - Pozicionirajte se u folder frontenda:
     ```bash
     cd frontend
     npm install
     ng serve
     ```

4. **Nginx i Baza podataka** (Opciono, konfiguracioni fajlovi se nalaze u `/deploy` folderu).

---

## 👨‍💻 Autori / Developed by

- [Teodora Nikolić](https://github.com/teodora525)

---

## 📄 Licenca

Distribuira se pod [MIT licencom](./LICENSE).

---

## 🎓 Krediti

Ovaj projekat je realizovan u okviru predmeta **Sistemi bazirani na znanju** na Fakultetu tehničkih nauka u Novom Sadu, smer Softversko inženjerstvo i informacione tehnologije (SIIT).
```