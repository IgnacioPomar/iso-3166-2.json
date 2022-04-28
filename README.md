# World countries subdivisions in JSON
## Countries subdivisions data
This repository contains a list of the principal subdivisions from the world countries, as defined by [ISO Standard 3166-2](https://en.wikipedia.org/wiki/ISO_3166-2), currently only in JSON.

## Numeric code
The data comes with a numeric code - called uid, from unique Identifier -, wich is not part of the iso, but is computed by the following:
- Takes the `ccn3` code  (From ISO 3166-1)
- Multiply the result by 10000
- If the cuntry only hace numeric divisions, then we use the numeric division as it is
- If not, then we convert the number to decimal code; For each char:
  - multiply the previous value by the length of the string `0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ`
  - add the index position of the char in the previous string

The MAX_CODE_VALUE would be 99946655 (for a country with ccn3 of 999 and a division code ??-ZZZ)

## Sources
Most comes from Wikipedia.
[https://www.iso.org/](https://www.iso.org/obp/ui/#iso:pub:PUB500001:en)  for cheking the data.

## Credits

Thanks to:  
 - @mlendoze for his work(https://github.com/mledoze/countries), wich bring the idea

## License
See  [LICENSE](https://github.com/IgnacioPomar/iso-3166-2.json/blob/master/LICENSE).
