# E-Dziennik-JavaFX

Podstawowa Aplikacja Szkolnego Dziennika Elektroniczego

Nie jest ona w żaden sposób przeznaczona komercyjnie i została tylko napisana w ramach chęci nauki.


//Instrukcja//
Aby odpalić aplikacje, trzeba odpalić runnera. Osoba testująca ma dostępne 3 konta testowe.
Konto UCZNIA
Login: TestU
haslo: 123

Konto Nauczyciela
Login: TestN
haslo: 123

Konto Admina
Login: TestA
haslo: 123

!Uwaga, uczen i nauczyciel nie mają przypisanych klas i trzeba to zrobić w panelu admina



Wykorzystałem:
Jave,Gson,JavaFX,Maven

Aplikacja umożliwia, rejestracje i logowanie się do kont.
Jako konto z uprawnieniami ADMINA, możesz działać na użytkownikach np. usuwać,zmieniać permisje, przypisywać klasy, bądź zmieniać hasła użytkowniką, tworzyć klasy i przedmioty w nich uczone. 

Jako uczeń możesz tylko podejrzeć swoje oceny z poszczególnych przedmiotów i sprawdzić średnią. 

Jako nauczyciel masz możliwość podglądu do listy uczniów w danej klasie, nauczyciele mogą widzieć tylko klasy do których są przypisani i przedmioty do których są przypisani. Mogą oni dodawać i usuwać oceny, oraz sprawdzać średnią poszczególnych uczniów.

Wszystkie Dane które są używane w aplikacji zapisywane są plikach .json i również z nich odczytywane. Klasy.json zapisują dane o danych klasach i przedmotach w nich nauczanych, konta.json zapisują dane o loginie, Id,roli i zahashowane hasło, nauczyciele.json zapisuje dane o nauczycielach takie jak imie, nazwisko, id  i klasy przypisane, studenci.json gdzie są dane o uczniach, ich ocenach i przypisanych klasach.
Do wykonania UI użyłem interfejsu graficznego JavaFX.

Folder DTO, służy do autoryzacji, czyli logiki logowania i rejestrowania kont oraz do w folderze "DaneSystemu", to wczytywania i zapisywania danych z plików Json.

Folder Konta, służy jako logika naszej bazy danych, wykorzystany gson jest łatwy w obsłudze i bardzo uniwersalny dlatego wybrałem go do tego projektu.

Folder Logic, odpowiada za logike w naszym oknie Admina i Nauczyciela, są w nim zawarte podstawowe funkcje dostępne w tych oknach.

Folder model, w tym folderze zawarte są obiekty takie jak student,teacher,klasa,konto na których działa każda pozostała funckja/klasa i stanowią fundament całego projektu.

Folder runner, służy tylko do zainicjowania z "DaneSystemu" i odpalenia interfejsu graficznego aplikacji.

Folder UI, obsługuje cały interfejs graficzny w JavaFX, zdecydowałem się na JavaFX gdyż pozwala w prosty sposób zbudować podstawową szate graficzna projektu i ma wiele możliwości a chciałem aby cały projekt był tylko w języku Java.

uprawnienia, to tylko plik z enumami uprawnien jakimi mogą być użytkownicy.

W org.src/Konta zapisane są nasze pliki json.







