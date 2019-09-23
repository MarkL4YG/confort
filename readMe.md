# Confort

> _Manage your configurations with comfort._

A configuration library based on nodes, trees and parsers generated from [ANTLR Grammars](https://www.antlr.org/).  

## Badges
|---------------------|-------------------------------------------------------------------------------------------------------------------------------------------|  
| BuddyCI (bleeding) | [![buddy pipeline](https://app.buddy.works/m-lessmann/confort/pipelines/pipeline/194465/badge.svg?token=22548d502f11240ea437ccc14a4348c352915b0cf82518920be9d2c98bdcb9dd "buddy pipeline")](https://app.buddy.works/m-lessmann/confort/pipelines/pipeline/194465) |  
| BuddyCI (pre-release)  | [![buddy pipeline](https://app.buddy.works/m-lessmann/confort/pipelines/pipeline/194480/badge.svg?token=22548d502f11240ea437ccc14a4348c352915b0cf82518920be9d2c98bdcb9dd "buddy pipeline")](https://app.buddy.works/m-lessmann/confort/pipelines/pipeline/194480) |  
| Snyk Security Check | [![Known Vulnerabilities](https://snyk.io/test/github/markl4yg/confort/badge.svg)](https://snyk.io/test/github/markl4yg/confort) |  
| Dependabot-Updates  | [![Dependabot Status](https://api.dependabot.com/badges/status?host=github&repo=MarkL4YG/confort)](https://dependabot.com) |  
| License             | ![GitHub](https://img.shields.io/github/license/markl4yg/confort.svg) [![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FMarkL4YG%2Fconfort.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2FMarkL4YG%2Fconfort?ref=badge_shield)|  


## Description
This library makes configuration easy by completely hiding away format specifications, loading and saving from its users.  
Registered loaders allow easy retrieval of the loader of the right format.
(E.g. ``LoaderRegistry.getLoader("application/json")``).  
  
Additionally, there are utility classes to hide away the type of storage. So no matter, where the configuration is actually loaded from, the calls will be the same.
  
Loaders can (but don't have to) be based on ANTLR grammars which are freely available [in the public repository](https://github.com/antlr/grammars-v4).  
Grammar for your format is not there? Don't give up! Writing ANTLR grammar is easy to learn.  

## Links

* Documentation: https://github.com/MarkL4YG/confort/wiki
* Issues: https://github.com/MarkL4YG/confort/issues
* Contact:  
  * [github@m-lessmann.de](mailto:github@m-lessmann.de)  
  * [ts.fearnixx.de](ts3server://ts.fearnixx.de)  
  * [Jeak-Framework Discord](https://discord.gg/DPYR5aB)
  
---
  
#### Buy me a coffe:
If you would like to thank me for my work, please consider donating a drink :)  

[![ko-fi](https://www.ko-fi.com/img/donate_sm.png)](https://ko-fi.com/F1F0OL0V)


## License
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2FMarkL4YG%2Fconfort.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2FMarkL4YG%2Fconfort?ref=badge_large)
