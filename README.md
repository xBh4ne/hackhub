# HackHub

HackHub è il backend REST per la gestione completa di hackathon: registrazione e autenticazione degli account, creazione e conduzione degli hackathon, formazione dei team, inviti, sottomissioni, valutazioni, proclamazione dei vincitori, richieste di supporto ai mentori e segnalazioni.

Il progetto è un'applicazione Java basata su Spring Boot, organizzata secondo un'architettura a livelli di ispirazione Domain-Driven Design. Non è inclusa un'interfaccia web: tutte le funzionalità sono esposte tramite API HTTP e documentate nella collection Postman presente nel repository.

## Indice

- [Funzionalità principali](#funzionalità-principali)
- [Stack tecnologico](#stack-tecnologico)
- [Architettura](#architettura)
- [Requisiti](#requisiti)
- [Avvio del progetto](#avvio-del-progetto)
- [Configurazione](#configurazione)
- [Autenticazione e autorizzazione](#autenticazione-e-autorizzazione)
- [Ruoli](#ruoli)
- [Ciclo di vita di un hackathon](#ciclo-di-vita-di-un-hackathon)
- [Ciclo di vita di un invito](#ciclo-di-vita-di-un-invito)
- [Riferimento API](#riferimento-api)
- [Esempi di utilizzo](#esempi-di-utilizzo)
- [Struttura del progetto](#struttura-del-progetto)
- [Postman](#postman)
- [Stato del progetto e limiti noti](#stato-del-progetto-e-limiti-noti)

## Funzionalità principali

- Registrazione e login con rilascio di token JWT.
- Gestione degli account utente e degli account staff con ruoli distinti.
- Creazione e configurazione degli hackathon da parte degli organizzatori.
- Apertura delle iscrizioni, avvio dell'hackathon, avvio e conclusione della valutazione, proclamazione del vincitore.
- Creazione dei team, invio e gestione degli inviti, ingresso e uscita dai team.
- Iscrizione dei team agli hackathon.
- Invio e aggiornamento delle sottomissioni da parte dei team.
- Valutazione delle sottomissioni da parte dei giudici.
- Richieste di supporto ai mentori e prenotazione di call.
- Segnalazioni dei team da parte dei mentori e consultazione da parte degli organizzatori.
- Gestione centralizzata degli errori tramite risposte JSON strutturate.

## Stack tecnologico

| Componente | Tecnologia |
| --- | --- |
| Linguaggio | Java 21 |
| Framework | Spring Boot 4.0.6 |
| Web | Spring MVC |
| Persistenza | Spring Data JPA, Hibernate |
| Database | H2 (file locale) |
| Sicurezza | Spring Security, JWT (JJWT 0.12.6) |
| Validazione | Jakarta Bean Validation |
| Build | Maven Wrapper |
| Utility | Lombok |

## Architettura

Il codice è suddiviso in tre macro-aree.

### Domain

Contiene il modello di business e le regole applicative:

- entità JPA (`Hackathon`, `Team`, `Utente`, `Staff`, `Invito`, `Sottomissione`, `Valutazione`, `RichiestaSupporto`, `Segnalazione`, `Call`);
- macchine a stati per hackathon e inviti, implementate con il pattern State;
- servizi di dominio;
- facade applicative che orchestrano servizi, validatori e mapper;
- validatori di ruoli e regole di appartenenza;
- interfacce dei repository e porte verso servizi esterni (`CalendarAdapter`, `PagamentoAdapter`).

### Infrastructure

Contiene le implementazioni tecniche:

- configurazione di Spring Security, PasswordEncoder e AuthenticationProvider;
- filtro JWT e provider dei token;
- convertitori JPA per gli stati;
- implementazioni degli adapter;
- gestione globale e specifica delle eccezioni;
- inizializzazione dei dati di sviluppo.

### Presentation

Espone l'API REST:

- controller suddivisi per area funzionale;
- DTO di richiesta e risposta;
- mapper tra entità di dominio e DTO.

## Requisiti

- JDK 21 installato e configurato in `JAVA_HOME`.
- Nessuna installazione Maven separata: il repository include il Maven Wrapper.
- Porta `8080` disponibile, salvo diversa configurazione.

## Avvio del progetto

Clonare il repository ed eseguire l'applicazione dalla directory principale:

```bash
./mvnw spring-boot:run
```

In alternativa, generare il pacchetto eseguibile:

```bash
./mvnw clean package
java -jar target/hack-hub-1.jar
```

L'applicazione risponde di default su `http://localhost:8080`.

Al primo avvio viene creato automaticamente un account amministratore di sviluppo:

| Username | Password | Ruolo |
| --- | --- | --- |
| `admin` | `admin` | `ADMIN` |

L'account viene creato solo se non esiste già. Le credenziali sono destinate esclusivamente allo sviluppo e devono essere rimosse o modificate prima di qualsiasi utilizzo in ambienti reali.

## Configurazione

La configurazione si trova in `src/main/resources/application.properties`.

| Proprietà | Valore di default | Descrizione |
| --- | --- | --- |
| `spring.datasource.url` | `jdbc:h2:file:./data/hackhub;AUTO_SERVER=TRUE` | Database H2 su file locale |
| `spring.jpa.hibernate.ddl-auto` | `update` | Aggiornamento automatico dello schema |
| `jwt.expiration` | `900` | Durata del token in secondi |
| `jwt.secret` | valore di sviluppo | Chiave di firma del token |
| `jwt.issuer` | `hack-hub` | Issuer del token |
| `jwt.audience` | `hack-hub-client` | Audience del token |
| `security.salt` | valore di sviluppo | Salt per la codifica delle password |

I valori presenti nel repository sono pensati per lo sviluppo locale. In ambienti condivisi o di produzione è necessario esternalizzare almeno credenziali del database, salt e chiave JWT, ad esempio tramite variabili d'ambiente (`JWT_SECRET`, `JWT_EXPIRATION`, `SECURITY_SALT`, `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`).

Il database viene creato nella directory `data/` al primo avvio. L'eliminazione di tale directory comporta la perdita dei dati salvati.

## Autenticazione e autorizzazione

L'autenticazione è stateless e basata su JWT.

1. L'utente effettua il login tramite `POST /api/auth/login`.
2. Il server restituisce un oggetto con `accessToken`, `expiration` e `type`.
3. Il token deve essere inviato nelle richieste protette con l'header:

```http
Authorization: Bearer <accessToken>
```

Le password sono codificate con PBKDF2-HMAC-SHA256. Le autorizzazioni sono applicate tramite annotazioni `@PreAuthorize` sui controller, mentre il filtro JWT popola il contesto di sicurezza di Spring.

Gli endpoint di registrazione e login, la lista degli hackathon, il dettaglio di un hackathon e il team vincitore sono accessibili senza autenticazione. Le altre operazioni richiedono un token valido e il ruolo appropriato.

## Ruoli

### Ruoli utente

| Ruolo | Descrizione |
| --- | --- |
| `UTENTE` | Account standard. Può creare un team, invitare membri, iscriversi agli hackathon, inviare sottomissioni e richiedere supporto. |
| `TEAM_MEMBER` | Ruolo previsto dal modello per i membri di un team. |
| `TEAM_LEADER` | Ruolo previsto dal modello per il leader di un team. |

### Ruoli staff

| Ruolo | Descrizione |
| --- | --- |
| `ADMIN` | Amministra la piattaforma, crea gli account staff e consulta tutti gli account, i team e le segnalazioni. |
| `ORGANIZZATORE` | Crea e gestisce gli hackathon, assegna i mentori, apre le iscrizioni, avvia le fasi e proclama il vincitore. |
| `GIUDICE` | Valuta le sottomissioni degli hackathon a cui è assegnato e conclude la fase di valutazione. |
| `MENTORE` | Gestisce le richieste di supporto dei team, prenota call e invia segnalazioni. |

## Ciclo di vita di un hackathon

```text
CREATO
  -> ISCRIZIONE
  -> IN_CORSO
  -> VALUTAZIONE
  -> PROCLAMAZIONE_VINCITORE
  -> CONCLUSO
```

| Stato | Operazioni consentite |
| --- | --- |
| `CREATO` | Apertura delle iscrizioni |
| `ISCRIZIONE` | Iscrizione dei team, avvio dell'hackathon |
| `IN_CORSO` | Invio delle sottomissioni, avvio della valutazione |
| `VALUTAZIONE` | Valutazione delle sottomissioni, conclusione della valutazione |
| `PROCLAMAZIONE_VINCITORE` | Proclamazione del vincitore |
| `CONCLUSO` | Nessuna ulteriore transizione |

La conclusione della valutazione richiede che tutte le sottomissioni siano state valutate. La proclamazione del vincitore richiede che tutte le sottomissioni siano state valutate e che l'operazione sia eseguita dall'organizzatore dell'hackathon.

## Ciclo di vita di un invito

```text
PENDENTE
  -> ACCETTATO
  -> RIFIUTATO
```

Un invito accettato o rifiutato non può più cambiare stato. L'accettazione è consentita solo a un utente che non appartiene già a un team. Non è possibile creare due inviti per la stessa coppia team-utente.

## Riferimento API

Tutti i percorsi sono relativi a `http://localhost:8080`.

### Autenticazione e account

| Metodo | Endpoint | Ruolo | Descrizione |
| --- | --- | --- | --- |
| `POST` | `/api/auth/login` | Pubblico | Effettua il login e restituisce il token |
| `POST` | `/api/auth/register` | Pubblico | Registra un nuovo utente con ruolo `UTENTE` |
| `POST` | `/api/auth/register/staff` | `ADMIN` | Crea un account staff specificando il ruolo |
| `GET` | `/api/auth/accounts` | `ADMIN` | Elenca tutti gli account utente e staff |

### Utente

| Metodo | Endpoint | Ruolo | Descrizione |
| --- | --- | --- | --- |
| `GET` | `/api/utente` | `UTENTE` | Restituisce il profilo dell'utente autenticato |
| `GET` | `/api/utente/inviti` | `UTENTE` | Elenca gli inviti pendenti ricevuti |

### Hackathon

| Metodo | Endpoint | Ruolo | Descrizione |
| --- | --- | --- | --- |
| `GET` | `/api/hackathons` | Pubblico | Elenca gli hackathon |
| `GET` | `/api/hackathons/{hackathonId}` | Pubblico | Restituisce il dettaglio di un hackathon |
| `POST` | `/api/hackathons` | `ORGANIZZATORE` | Crea un hackathon |
| `POST` | `/api/hackathons/{hackathonId}/mentore/add/{mentoreId}` | `ORGANIZZATORE` | Assegna un mentore all'hackathon |
| `POST` | `/api/hackathons/{hackathonId}/mentori/remove/{mentoreId}` | `ORGANIZZATORE` | Rimuove un mentore dall'hackathon |
| `POST` | `/api/hackathons/{hackathonId}/apri-iscrizioni` | `ORGANIZZATORE` | Apre le iscrizioni |
| `POST` | `/api/hackathons/{hackathonId}/iscrivi` | `UTENTE`, `TEAM_LEADER` | Iscrive il team dell'utente |
| `POST` | `/api/hackathons/{hackathonId}/avvia` | `ORGANIZZATORE` | Avvia l'hackathon |
| `POST` | `/api/hackathons/{hackathonId}/avvia-valutazione` | `ORGANIZZATORE` | Avvia la fase di valutazione |
| `POST` | `/api/hackathons/{hackathonId}/concludi-valutazione` | `GIUDICE` | Conclude la valutazione |
| `POST` | `/api/hackathons/{hackathonId}/proclama-vincitore` | `ORGANIZZATORE` | Proclama il team vincitore |
| `GET` | `/api/hackathons/{hackathonId}/team-vincitore` | Pubblico | Restituisce il team vincitore |

### Team

| Metodo | Endpoint | Ruolo | Descrizione |
| --- | --- | --- | --- |
| `GET` | `/api/teams` | `ADMIN` | Elenca tutti i team |
| `POST` | `/api/teams` | `UTENTE` | Crea un team |
| `POST` | `/api/teams/{hackathonId}/paga-vincitore` | `ORGANIZZATORE` | Avvia il pagamento del premio |
| `GET` | `/api/team` | `UTENTE` | Restituisce il team dell'utente autenticato |
| `POST` | `/api/team/esci` | `UTENTE` | Esce dal team; se è l'ultimo membro, il team viene eliminato |
| `GET` | `/api/team/inviti` | `UTENTE` | Elenca gli inviti pendenti inviati dal team |
| `GET` | `/api/team/hackathons` | `UTENTE` | Elenca gli hackathon a cui il team è iscritto |
| `GET` | `/api/team/sottomissioni` | `UTENTE` | Elenca le sottomissioni del team |
| `GET` | `/api/team/richieste-supporto` | `UTENTE` | Elenca le richieste di supporto del team |

### Inviti

| Metodo | Endpoint | Ruolo | Descrizione |
| --- | --- | --- | --- |
| `GET` | `/api/inviti` | `ADMIN`, `UTENTE` | Elenca gli inviti pendenti; l'utente vede solo i propri |
| `POST` | `/api/inviti` | `UTENTE` | Invita un utente nel proprio team |
| `POST` | `/api/inviti/{invitoId}/accetta` | `UTENTE` | Accetta un invito |
| `POST` | `/api/inviti/{invitoId}/rifiuta` | `UTENTE` | Rifiuta un invito |

### Sottomissioni e valutazioni

| Metodo | Endpoint | Ruolo | Descrizione |
| --- | --- | --- | --- |
| `GET` | `/api/sottomissioni/{sottomissioneId}` | `GIUDICE`, `ORGANIZZATORE`, `UTENTE` | Restituisce una sottomissione |
| `POST` | `/api/sottomissioni/send` | `UTENTE` | Invia una nuova sottomissione |
| `POST` | `/api/sottomissioni/update` | `UTENTE` | Aggiorna la sottomissione del team |
| `POST` | `/api/sottomissioni/{sottomissioneId}/valuta` | `GIUDICE` | Valuta o aggiorna la valutazione di una sottomissione |

### Staff

| Metodo | Endpoint | Ruolo | Descrizione |
| --- | --- | --- | --- |
| `GET` | `/api/staffs` | `ADMIN` | Elenca tutti gli account staff |
| `GET` | `/api/staffs/admins` | `ADMIN` | Elenca gli amministratori |
| `GET` | `/api/staffs/giudici` | `ADMIN`, `ORGANIZZATORE` | Elenca i giudici |
| `GET` | `/api/staffs/organizzatori` | `ADMIN` | Elenca gli organizzatori |
| `GET` | `/api/staffs/mentori` | `ADMIN`, `ORGANIZZATORE` | Elenca i mentori |
| `GET` | `/api/staffs/hackathons` | `GIUDICE`, `ORGANIZZATORE`, `MENTORE` | Elenca gli hackathon assegnati allo staff |
| `GET` | `/api/staffs/sottomissioni` | `GIUDICE` | Elenca le sottomissioni da valutare |

### Supporto e segnalazioni

| Metodo | Endpoint | Ruolo | Descrizione |
| --- | --- | --- | --- |
| `GET` | `/api/richieste-supporto` | `MENTORE` | Elenca le richieste di supporto assegnate al mentore |
| `POST` | `/api/richieste-supporto/hackathon/{hackathonId}/mentore/{mentoreId}` | `UTENTE` | Crea una richiesta di supporto |
| `POST` | `/api/richieste-supporto/{richiestaId}/call` | `MENTORE` | Prenota una call per una richiesta |
| `GET` | `/api/segnalazioni` | `ORGANIZZATORE`, `ADMIN` | Elenca le segnalazioni gestibili |
| `POST` | `/api/segnalazioni/hackathon/{hackathonId}/team/{teamId}` | `MENTORE` | Segnala un team |
| `GET` | `/api/segnalazioni/team/{teamId}` | `ORGANIZZATORE` | Elenca le segnalazioni di un team |

### Formato degli errori

Gli errori applicativi sono restituiti con una struttura JSON omogenea:

```json
{
  "code": 400,
  "status": "Bad Request",
  "message": "Descrizione dell'errore",
  "timestamp": "27/10/2026 14:30:00"
}
```

I codici HTTP utilizzati includono `400`, `401`, `403`, `404`, `405` e `409`.

## Esempi di utilizzo

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'
```

### Registrazione di un utente

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"mario.rossi","email":"mario.rossi@example.com","password":"password"}'
```

### Creazione di un team

```bash
curl -X POST http://localhost:8080/api/teams \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <accessToken>" \
  -d '{"name":"Team Alfa","descrizione":"Team di sviluppo"}'
```

### Creazione di un hackathon

```bash
curl -X POST http://localhost:8080/api/hackathons \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <accessToken>" \
  -d '{
    "nome":"HackHub 2026",
    "descrizione":"Hackathon dimostrativo",
    "regolamento":"Regolamento dell'evento",
    "dataInizio":"02/05/2026 09:30",
    "dataFine":"01/09/2026 09:30",
    "dataScadenzaIscrizioni":"01/05/2026 09:00",
    "luogo":"Camerino",
    "premioDenaro":1000,
    "dimMaxTeam":10,
    "giudiceId":1
  }'
```

Le date sono accettate nel formato `dd/MM/yyyy HH:mm`.

## Struttura del progetto

```text
hackhub/
├── src/main/java/unicam/dreamteam/
│   ├── domain/
│   │   ├── adapter/          Porte verso servizi esterni
│   │   ├── exception/        Eccezioni di dominio
│   │   ├── model/            Entità, builder e macchine a stati
│   │   ├── repository/       Interfacce Spring Data JPA
│   │   ├── service/          Servizi di dominio e facade
│   │   └── validator/        Validatori applicativi
│   ├── infrastructure/
│   │   ├── adapter/          Implementazioni degli adapter
│   │   ├── api/              Strutture di risposta
│   │   ├── config/           Configurazione applicativa e di sicurezza
│   │   ├── exception/        Gestione HTTP delle eccezioni
│   │   └── persistence/      Convertitori JPA
│   └── presentation/
│       ├── api/controllers/  Controller REST
│       ├── dto/              DTO di richiesta e risposta
│       └── mapper/           Mapping dominio-DTO
├── src/main/resources/
│   └── application.properties
├── hack-hub.postman_collection.json
├── hack-hub.vpp
├── pom.xml
├── mvnw
└── mvnw.cmd
```

Il file `hack-hub.vpp` contiene il progetto Visual Paradigm con i modelli di analisi e progettazione.

## Postman

La collection `hack-hub.postman_collection.json` raccoglie le richieste per tutti i ruoli e i principali flussi applicativi.

Per utilizzarla:

1. importare il file in Postman;
2. valorizzare le variabili della collection;
3. eseguire il login per ottenere il token;
4. assegnare il token alla variabile `ACCESS_TOKEN`.

Valori consigliati per un'istanza locale:

| Variabile | Valore |
| --- | --- |
| `BASE_URL` | `http://localhost:8080/api` |
| `BASE_AUTH_URL` | `http://localhost:8080/api/auth` |
| `LOGIN_URL` | `http://localhost:8080/api/auth/login` |
| `REGISTER_UTENTE_URL` | `http://localhost:8080/api/auth/register` |

Le variabili relative a username e password dei diversi ruoli devono essere impostate con account creati nel sistema.

## Stato del progetto e limiti noti

- Le integrazioni con il sistema di prenotazione delle call (`CalendarAdapter`) e con il sistema di pagamento (`PagamentoAdapter`) sono implementazioni segnaposto e sollevano un'eccezione se invocate. Gli endpoint che le utilizzano non sono quindi operativi end-to-end.
- I ruoli `TEAM_MEMBER` e `TEAM_LEADER` sono definiti nel modello, ma la creazione di un team non modifica il ruolo dell'utente, che rimane `UTENTE`.
- Il limite massimo di membri per team (`dimMaxTeam`) è memorizzato nell'hackathon ma non viene applicato al momento dell'invito o dell'accettazione.
- Non è presente una suite di test automatizzati nel repository.
- Il database H2 con `ddl-auto=update` è adatto allo sviluppo e alla dimostrazione, non a un ambiente di produzione.
- Le credenziali e le chiavi presenti in `application.properties` sono valori di sviluppo e devono essere sostituite.
- Alcuni endpoint accettano o restituiscono testo semplice, ad esempio la segnalazione di un team e il pagamento del vincitore.

## Licenza

Il repository non contiene attualmente un file di licenza. Ogni utilizzo, distribuzione o modifica deve essere concordato con gli autori del progetto.
