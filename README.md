## Birb.app

### Készült az SZTE Mobil alkalmazásfejlesztés (IB470G-2) kurzusára.

A Birb.app egy twitter klón. 
A felhasználók regisztrálhatnak, majd egy közös üzenőfalra postokat írhatnak, törölhetnek, vagy mások postjait repostolhatják, lájkolhatják.

#### Javítási segédlet, [a google sheet alapján](https://docs.google.com/spreadsheets/d/1EjF3BcORhPtaLhDuegIORdjlUkcDQRfUdq1m6Tpxc48)

- **Fordítási/Futtatási hiba nincs**: A fordításos egy érdekes követelmény, mivel a `google-services.json` nélkül nem fordul a projekt, és [köztudott, hogy ezt nem érdemes megosztani nyilvános repositoryban az API kulcsok miatt.](https://stackoverflow.com/questions/37358340/should-i-add-the-google-services-json-from-firebase-to-my-repository)

tl;dr fordításnál nézd meg, hogy ilyen errort dob-e I guess:
```
Execution failed for task ':app:processDebugGoogleServices'.
> File google-services.json is missing. The Google Services Plugin cannot function without it.
```

- **Firebase autentikáció meg van valósítva, be lehet jelentkezni és regisztrálni**: ``MainActivity.java:35`` és ``RegisterActivity.java`` felelős ezekért. Login és regisztáció elérhető az app kezdőképernyőjén:
![image](https://user-images.githubusercontent.com/80174357/236635719-ca0bcd8e-acf9-4b47-8ec1-26fc49bd824d.png)

- **Adatmodell definiálása (class vagy interfész formájában)**: ``PostModel.java`` és ``UserModel.java``.

- **Legalább 3 különböző activity vagy fragmens használata**: `app/java` mappában lévő ``MainActivity.java``, ``CreatePost.java``, ``RegisterActivity.java`` és ``UserFeed.java`` mindegyike egy activity.

- **Beviteli mezők beviteli típusa megfelelő (jelszó kicsillagozva, email-nél megfelelő billentyűzet jelenik meg stb.)**: Mindenhol megvan, a `res/layout` mappa `activity_register` és `activity_main` mappákban, de a felületen is látszania kéne.

- **ConstraintLayout és még egy másik layout típus használata**: `res/layout/activity_main.xml` Constraint layoutot, a `res/layout/post.xml` pedig Relative layoutot használ.

- **Reszponzív (...)*: Az `activity_register` és `activity_main` layoutoknak van külön land változata, döntött képernyőre. Kisebb képernyőjű emulátoron sem csúszik szét: ![image](https://user-images.githubusercontent.com/80174357/236636496-a7767498-8053-45e5-91a8-5862a327f4c6.png)

- **Legalább 2 különböző animáció használata**: Login képernyőn felül a logón, illetve bejelentkezés után a postok betöltésénél vannak használva. `res/anim/pulse.xml` és `res/anim/spin.xml` respektíven.

- **Intentek használata: navigáció meg van valósítva az activityk/fragmensek között (minden activity/fragmens elérhető)**: Minden activity elérhető, a `UserFeed` viszont csakis bejelentkezés után. A `CreatePost` a `UserFeed` tetején lévő + ikonnal elérhető:
![image](https://user-images.githubusercontent.com/80174357/236636701-0c0550da-d7ec-45d7-aa5c-b46968671b61.png)

- **Legalább egy Lifecycle Hook használata a teljes projektben**: ``UserFeed.java:75``. Az OnRestart frissíti a postok listáját, ha appot váltunk, majd vissza

- **Legalább egy olyan androidos erőforrás használata, amihez kell android permission**: Ilyen nincs.

- **Legalább egy notification vagy alam manager vagy job scheduler használata**: ``UserFeed.java:97``. A ``setupPostUpdateAlarm()`` időközönként frissíti a postok listáját (kb egy percenként)

- **CRUD műveletek mindegyike megvalósult és az adatbázis műveletek a konvenciónak megfelelően külön szálon történnek**:
Lehet egy műveletből több is van, itt most csak példákat hoztam. **Nincsennek külön szálon.**
  - CREATE: ``RegisterActivity.java:68``, ahol regisztráció után egy új firestoreos usert viszünk fel.
  - READ: ``UserFeed.java:81``, lekérjük/frissítjük a postok listáját
  - UPDATE: ``PostHolder.java:106``, ``likePost()``, a like utáni callback updateolja a likeok számát.
  - DELETE: ``PostHolder.java:150``, ``deletePost()``, a saját posztjaink törlésének logikája
  
- **Legalább 2 komplex Firestore lekérdezés megvalósítása, amely indexet igényel (ide tartoznak: where feltétel, rendezés, léptetés, limitálás)**:
  - ``UserFeed.java:85``: postok `date` szerint rendezve
  - ``CreatePost.java:46``: user lekérése, `limit(1)`, és `where()`
  
- **Szubjektív pontozás a projekt egészére vonatkozólag**: pls bro 5/5












