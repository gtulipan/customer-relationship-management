## crm-4ig

#### Üzleti igény:
Az alkalmazás rövid leírása:
Java backend modul, mely megvalósít egy egyszerű partner nyilvántartó rendszert, melyet REST végpontokon (vagy más általad ismert kommunikációval) lehet használni.
Az alkalmazás elvárt funkcionalitása:
1.
Lehessen az alkalmazásba bejelentkezni felhasználónév és jelszóval
2.
Partnerek (ügyfelek) karbantartása, legyen lehetőség:
a.
az adatok listázására,
b.
az adatok kereshetőségére (partner keresése cím alapján is),
c.
az adatok exportálására,
d.
illetve az adatok felvételére, módosítására, törlésére.
3.
Partnerekhez tartozó címek karbantartása, legyen lehetőség:
a.
az adatok listázására,
b.
az adatok kereshetőségére,
c.
az adatok exportálására,
d.
illetve az adatok felvételére, módosítására, törlésére.
4.
A Partnerek és a címek egy a többes kapcsolatban álljanak

#### Előfeltétel:
A fordításhoz Gradle szükséges.
A konténerizáció Dockert használ.
(A project SpringBoot 3.4-es framework-kel és Java 21-gyel készült.)


#### Indítás:
A project letöltése után a crm-4ig root mappában a `./gradlew clean build --refresh-dependencies` parancsot adjuk ki.
Sikeres futtatása után a `docker-compose build --no-cache; docker-compose up -d` parancsot adjuk ki, szintén a crm-4ig root mappában.

#### Tesztelés:
A `http://localhost:8080/swagger-ui/index.html` oldalt használhatjuk.

Swagger-es tesztelés esetén a felkínált Json-t kell módosítani/bővíteni.

Az adatbázis induláskor egy "user@examle.hu" teszt felhasználót tartalmaz akinek a jelszava "user".
Az adatbázis tartalmaz továbbá egy "admin@example.com" teszt admin felhasználót, de jelenleg nincs olyan funkció, mely admin jogokhoz kötött.

#### Továbbfejlesztés:
Angular kliens elkészítése. A back end jelenleg fel van készítve egy Angular kliensre.
CSRF és CORS funkcionalítások, valamint a JWT token generálás és kezelés be lett konfigurálva.