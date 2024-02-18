package io.jobmarks.jobscrapeservice.common.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Country {
    ARGENTINA("argentina", "ar", "com.ar"),
    AUSTRALIA("australia", "au", "com.au"),
    AUSTRIA("austria", "at", "at"),
    BAHRAIN("bahrain", "bh"),
    BELGIUM("belgium", "be", "fr:be"),
    BRAZIL("brazil", "br", "com.br"),
    CANADA("canada", "ca", "ca"),
    CHILE("chile", "cl"),
    CHINA("china", "cn"),
    COLOMBIA("colombia", "co"),
    COSTARICA("costa rica", "cr"),
    CZECHREPUBLIC("czech republic,czechia", "cz"),
    DENMARK("denmark", "dk"),
    ECUADOR("ecuador", "ec"),
    EGYPT("egypt", "eg"),
    FINLAND("finland", "fi"),
    FRANCE("france", "fr", "fr"),
    GERMANY("germany", "de", "de"),
    GREECE("greece", "gr"),
    HONGKONG("hong kong", "hk", "com.hk"),
    HUNGARY("hungary", "hu"),
    INDIA("india", "in", "co.in"),
    INDONESIA("indonesia", "id"),
    IRELAND("ireland", "ie", "ie"),
    ISRAEL("israel", "il"),
    ITALY("italy", "it", "it"),
    JAPAN("japan", "jp"),
    KUWAIT("kuwait", "kw"),
    LUXEMBOURG("luxembourg", "lu"),
    MALAYSIA("malaysia", "malaysia"),
    MEXICO("mexico", "mx", "com.mx"),
    MOROCCO("morocco", "ma"),
    NETHERLANDS("netherlands", "nl", "nl"),
    NEWZEALAND("new zealand", "nz", "co.nz"),
    NIGERIA("nigeria", "ng"),
    NORWAY("norway", "no"),
    OMAN("oman", "om"),
    PAKISTAN("pakistan", "pk"),
    PANAMA("panama", "pa"),
    PERU("peru", "pe"),
    PHILIPPINES("philippines", "ph"),
    POLAND("poland", "pl"),
    PORTUGAL("portugal", "pt"),
    QATAR("qatar", "qa"),
    ROMANIA("romania", "ro"),
    SAUDIARABIA("saudi arabia", "sa"),
    SINGAPORE("singapore", "sg", "sg"),
    SOUTHAFRICA("south africa", "za"),
    SOUTHKOREA("south korea", "kr"),
    SPAIN("spain", "es", "es"),
    SWEDEN("sweden", "se"),
    SWITZERLAND("switzerland", "ch", "de:ch"),
    TAIWAN("taiwan", "tw"),
    THAILAND("thailand", "th"),
    TURKEY("turkey", "tr"),
    UKRAINE("ukraine", "ua"),
    UNITEDARABEMIRATES("united arab emirates", "ae"),
    UK("uk,united kingdom", "uk", "co.uk"),
    USA("usa,us,united states", "www", "com"),
    URUGUAY("uruguay", "uy"),
    VENEZUELA("venezuela", "ve"),
    VIETNAM("vietnam", "vn");

    private final String[] value;

    Country(String... value) {
        this.value = value;
    }

    public static Country fromString(String countryStr) {
        countryStr = countryStr.trim().toLowerCase();
        for (Country country : values()) {
            String[] countryNames = country.value[0].split(",");
            if (Arrays.asList(countryNames).contains(countryStr)) {
                return country;
            }
        }

        List<String> validCountries = Arrays.stream(values()).map(c -> c.value[0]).collect(Collectors.toList());
        throw new IllegalArgumentException(
                String.format("Invalid country string: '%s'. Valid countries are: %s", countryStr, String.join(", ", validCountries))
        );
    }

    public String getIndeedDomainValue() {
        return value[1];
    }

    public String getGlassdoorDomainValue() {
        if (value.length == 3) {
            String[] subdomainAndDomain = value[2].split(":");
            String subdomain = subdomainAndDomain[0];
            String domain = (subdomainAndDomain.length > 1) ? subdomainAndDomain[1] : value[2];

            if (subdomain != null && domain != null) {
                return (subdomain.isEmpty()) ? String.format("www.glassdoor.%s", domain) : String.format("%s.glassdoor.%s", subdomain, domain);
            }
        } else {
            throw new RuntimeException("Glassdoor is not available for " + this.name());
        }

        return null;
    }

    public String getUrl() {
        return String.format("https://%s/", getGlassdoorDomainValue());
    }
}

