
# 1 Layman’s Summary

In deze thesis staan topologisch isolatoren centraal. Topologische isolatoren zijn materialen
die geen stroom geleiden, zoals een gewone isolater (bijvoorbeeld rubber), behalve aan de
randen van het materiaal. Wat deze materialen zo speciaal maakt is dat deze geleidende
rand niet alleen een randverschijnsel is, maar ook een eigenschap van de materie zelf. Dit
betekent dat deze geleidende rand ~~robuust is tegen~~ [anglicisme] **bestand is tegen** lokale verstoring aan de rand, dit feno-
meen wordt topologische bescherming genoemd. De relevantie van de topologische isolatoren
komt voort uit hun speciale eigenschappen en mogelijke toepassingen. Een van de mogelijke
toepassingen van deze materialen is in het opkomende gebied van de **Kwantumcomputer** [één woord].
**Kwantumcomputers** maken gebruik van de **kwantum aard** [duale karakter? of is dat wat anders] van het elektron om bepaalde
berekeningen te kunnen doen die gewone computers niet kunnen doen. Voor deze **kwantumcomputers** 
heb je stabiele qubits nodig, en dit is een van de grote drempels waar men nu te-
gen aanloopt. De robuuste toestanden aan de rand van een topologische isolator zouden hier
van toepassing kunnen **zijn**. Een andere eigenschap is dat deze randtoestanden stroom
geleiden zonder energieverlies, vanwege de topologische bescherming van deze toestanden.
Dit betekent dat er geen hitte geproduceerd wordt tijdens de geleiding. In computers is dit
warmteverlies de voornaamste beperking in de efficiëntie en de ~computatiekracht~ **rekenkracht**[?]. Topolo-
gische isolatoren zouden dus gebruikt kunnen worden om de efficiëntie en rekenkracht [ja, dat] van
computers te vergroten.

In 1980 ontdekte Klaus von Klitzing het **Kwantum-Hall-effect** [niet één woord], dat stelt dat bij een sterk mag-
netisch veld en bij temperaturen rond het absolute nulpunt er plateaus van spanningsverschil
waargenomen worden loodrecht op een toegepaste spanning [1] Twee jaar later voorspelde
Thouless, Kohomoto, Nightingale en den Nijs dat het **Kwantum-Hall-effect** in theorie deze to-
pologische isolatoren kon realiseren [2]. Voor dit effect heb je zowel een hele lage temperatuur
als sterke magneetvelden nodig, maar in 1988 heeft F. Haldane aangetoond dat een verge-
lijkbaar effect plaatsvindt zonder sterk magnetisch veld als er een specifieke symmetrie van
je systeem ~~gebroken~~ **verbroken** wordt [3]. In 2005, hebben Kane en Mele theoretisch voorspeld dat in
grafeen, een enkele platte laag koolstofatomen, deze symmetrie ~~gebroken~~ **verbroken** is bij temperaturen
laag genoeg en dat grafeen dan inderdaad een topologische isolator is [4, 5]. Helaas hadden
zij de sterkte van dit effect overschat en was dit effect niet waar te nemen. Desondanks heeft
hun publicatie aangezet tot een opleving van het onderzoek naar topologische isolatoren. En
sindsdien zijn er meerdere verschillende topologische isolatoren waargenomen. Een van deze
waarnemingen was in de experimenten van Reis et al. [6]. Zij ontdekten dat een bismutheen
systeem, een enkele platte laag van ~~bismuth atomen~~ **bismut-atomen~~ [geen h], een topologisch isolator vormt. Hun
experiment was speciaal omdat het op kamertemperatuur is uitgevoerd. Waar er eerst een
sterk magneetveld en een hele lage temperatuur nodig waren om een topologisch isolator te
vinden, waren die allebei niet meer vereist in het experiment van Reis et al.

Sindsdien bestaat er een methode om topologische isolatoren te voorspellen aan de hand van
alleen de dimensie en ~~symmetriën~~ **symmetrieën** van je systeem [7]. De **dimensie** van een object zegt iets over
de manier waarop iets schaalt, als voorbeeld: een lijn is 1-dimensionaal, een oppervlakte 2-
dimensionaal, en een volume 3-dimensionaal. Maar er zijn structuren die worden beschreven
met een **dimensie** die tussen deze geheeltallige dimensies in liggen. Zulke structuren worden
fractalen genoemd, en voor dit soort structuren bestaat er nog geen **classificatiemethode** [één woord].
Fractalen klinken erg abstract, maar ze kennen hun ~~origine~~ **oorsprong** juist in de natuur. Een van de
eerste om deze structuren te bestuderen was L. F. Richardson tijdens zijn poging om de
lengte van een landsgrens vast te stellen [8]. Hoewel dit paradoxaal bleek, zag hij wel dat
de schaling van een landsgrens beschreven kon worden met een getal tussen 1 en 2. Later
heeft B. Mandelbrot hier op voortgebouwd en ~~introduceerde~~ de term fractaal **geïntroduceerd**. Een van de
belangrijkste eigenschappen van een fractaal is dat het geheel lijkt op een kleiner onderdeel
van zichzelf, dit wordt zelfgelijkendheid genoemd. Er bestaat een subklasse van fractalen die
niet alleen ~~zelfgelijkendheid~~ **zelfgelijkend** zijn, maar waar het geheel identiek is aan een kleiner onderdeel.
De ~~Sierpinski driehoek~~ **Sierpiński-driehoek** is een van dat type fractalen. Een ~~Sierpinski driehoek~~ **Sierpiński-driehoek** kan gemaakt
worden door een gelijkzijdige driehoek te verdelen in vier kleinere gelijkvormige driehoeken
en de middelste weg te laten. Als je dit dan tot in het oneindige herhaalt voor ~~ieder over
gebleven~~ **iedere overgebleven** driehoek krijg je de ~~Sierpinski driehoek~~ **Sierpiński-driehoek**. De eerste 4 stappen staan afgebeeld in
figuur 1.

       Figuur 1: De eerste vier stappen in het genereren van een a Sierpinski driehoek.

In deze thesis zullen we twee onderzoekspaden bewandelen. Maar eerst ~~,~~ [weg die komma] hebben we de his-
torisch belangrijke resultaten van Haldane en Kane & Mele nagelopen in hoofdstuk 2 van
deze thesis. Vervolgens wordt in hoofdstuk 3 het eerste pad bewandeld, hier zullen we een
uitgebreider model opstellen ten opzichte van hoofdstuk 2 waarin we drie extra, meer energe-
tische, orbitalen per atoom meenemen. Met dit model hebben we gekeken of het meenemen
van deze extra orbitalen er ook voor zorgt dat extra ~~topologsich~~ **topologische** toestanden waargenomen
kunnen worden. Dit hebben we onderzocht voor een ~~grafeen model~~ **grafeenmodel** en een model voor een
enkele platte laag ~~bismuth~~ **bismut**. Hier hebben we inderdaad topologisch toestanden gevonden die
buiten het bereik van het eerdere model vielen. Vervolgens hebben we dit model ook gebruikt
om de resultaten van de kamertemperatuur topologische isolator [misschien 'op kamertemperatuur' erachter ipv ervoor] zoals onderzocht door Reis
et al. [6] te reproduceren.

Na een introductie van fractalen en een bespreking van een simpel model op een ~~Sierpinski
driehoek~~ **Sierpiński-driehoek** in hoofdstuk 4, zal het andere onderzoekspad bewandeld worden in hoofdstuk 5.
Hier onderzoeken we een model waarin we dezelfde effecten meenemen als F. Haldane heeft
meegenomen in zijn belangrijke onderzoek uit 1988 [3], maar dan in de fractale structuur
van de ~~Sierpinski driehoek~~ **Sierpiński-driehoek**. Hier stellen we een fasediagram op zoals Haldane ook heeft ge-
daan voor zijn model. In dit fasediagram wordt afgebeeld of voor een bepaalde combinatie
aan parameters een systeem een topologische isolator is. Voor een structuur verwant aan de
~~Sierpinski driehoek~~ **Sierpiński-driehoek** is dit eerder ook al gedaan door J. Li et al. [9], en zij vonden een samenge-
drukte variant van het fasediagram dat Haldane had gevonden. Maar J. Li et al. gebruikten
twee kopieën van de ~~Sierpinski driehoek~~ **Sierpiński-driehoek** die ze verbonden langs één rand om het probleem te
versimpelen. Door deze simplificatie verliezen ze de zelfgelijkendheid en introduceren ze een
symmetrie die in ~~het~~ **de** echte fractaal juist verloren gaat. Wij hebben gekeken naar ~~het~~ **de** echte
fractaal en hebben veel complexere fasediagrammen gevonden dan in de referentie [9]. Ook
worden er herhalende patronen waargenomen in de fasediagrammen, dit is indicatief voor
de zelfgelijkendheid van de ~~Sierpinski driehoek~~ **Sierpiński-driehoek**. Dit is een nieuw en origineel resultaat ~~en~~ **dat**
laat zien hoe belangrijk het behouden van de fractale aard is voor het onderzoeken van de
topologische eigenschappen van zulke fractale systemen.
