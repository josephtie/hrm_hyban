# PersonnelControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**cherchPersonnel**](#cherchpersonnel) | **GET** /api/personnels/cherchpersonnel | |
|[**cherchpersonMatricule**](#cherchpersonmatricule) | **GET** /api/personnels/cherchpersonMatricule | |
|[**cherchpersonNumCnps**](#cherchpersonnumcnps) | **GET** /api/personnels/cherchpersonNumCnps | |
|[**deleteCategory**](#deletecategory) | **POST** /api/personnels/supprimerpersonnel | |
|[**deleteUse**](#deleteuse) | **GET** /api/personnels/effectifPersonnel | |
|[**departpersonnel**](#departpersonnel) | **POST** /api/personnels/depart | |
|[**effectifUse**](#effectifuse) | **GET** /api/personnels/effectifparsite | |
|[**getPersonnelListJSON1**](#getpersonnellistjson1) | **GET** /api/personnels/listpersonneljson | |
|[**satRetraite**](#satretraite) | **GET** /api/personnels/stat/effectifPersonnelRetraite | |
|[**saveMersonnel**](#savemersonnel) | **POST** /api/personnels/enregistrerpersonnel | |
|[**selectPersonnel**](#selectpersonnel) | **POST** /api/personnels/choisirpersonnel | |
|[**selectPersonnelpret**](#selectpersonnelpret) | **POST** /api/personnels/choisirpersonnelpret | |
|[**statConge**](#statconge) | **GET** /api/personnels/stat/conge | |
|[**statMoyenneAge**](#statmoyenneage) | **GET** /api/personnels/stat/moyenAge | |
|[**statmassesalarialetypContrat**](#statmassesalarialetypcontrat) | **GET** /api/personnels/stat/massesalariale | |
|[**stattypContrat**](#stattypcontrat) | **GET** /api/personnels/stat/typecontrat | |
|[**updatePersonnel**](#updatepersonnel) | **POST** /api/personnels/modifierpersonnel | |
|[**updatePersonnel1**](#updatepersonnel1) | **POST** /api/personnels/editerpersonnel | |

# **cherchPersonnel**
> PersonnelDTO cherchPersonnel()


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let idPersonnel: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.cherchPersonnel(
    idPersonnel
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idPersonnel** | [**number**] |  | (optional) defaults to undefined|


### Return type

**PersonnelDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **cherchpersonMatricule**
> PersonnelDTO cherchpersonMatricule()


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let matri: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.cherchpersonMatricule(
    matri
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **matri** | [**string**] |  | (optional) defaults to undefined|


### Return type

**PersonnelDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **cherchpersonNumCnps**
> PersonnelDTO cherchpersonNumCnps()


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let cnps: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.cherchpersonNumCnps(
    cnps
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **cnps** | [**string**] |  | (optional) defaults to undefined|


### Return type

**PersonnelDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **deleteCategory**
> PersonnelDTO deleteCategory()


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.deleteCategory(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**PersonnelDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **deleteUse**
> string deleteUse()


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let id: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.deleteUse(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|


### Return type

**string**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **departpersonnel**
> PersonnelDTO departpersonnel()


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let idperso: number; // (default to undefined)

const { status, data } = await apiInstance.departpersonnel(
    idperso
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idperso** | [**number**] |  | defaults to undefined|


### Return type

**PersonnelDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **effectifUse**
> string effectifUse()


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let id: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.effectifUse(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|


### Return type

**string**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getPersonnelListJSON1**
> PersonnelDTO getPersonnelListJSON1()


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let limit: number; // (optional) (default to undefined)
let offset: number; // (optional) (default to undefined)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getPersonnelListJSON1(
    limit,
    offset,
    search
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **limit** | [**number**] |  | (optional) defaults to undefined|
| **offset** | [**number**] |  | (optional) defaults to undefined|
| **search** | [**string**] |  | (optional) defaults to undefined|


### Return type

**PersonnelDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **satRetraite**
> string satRetraite()


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let id: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.satRetraite(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|


### Return type

**string**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **saveMersonnel**
> ContratPersonnelDTO saveMersonnel(personnelRequest)


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration,
    PersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let personnelRequest: PersonnelRequest; //

const { status, data } = await apiInstance.saveMersonnel(
    personnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **personnelRequest** | **PersonnelRequest**|  | |


### Return type

**ContratPersonnelDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **selectPersonnel**
> Personnel selectPersonnel(idRequest)


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.selectPersonnel(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**Personnel**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **selectPersonnelpret**
> Personnel selectPersonnelpret(idRequest)


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.selectPersonnelpret(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**Personnel**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **statConge**
> string statConge()


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let id: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.statConge(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|


### Return type

**string**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **statMoyenneAge**
> string statMoyenneAge()


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let aid: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.statMoyenneAge(
    aid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **aid** | [**number**] |  | (optional) defaults to undefined|


### Return type

**string**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **statmassesalarialetypContrat**
> string statmassesalarialetypContrat()


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let id: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.statmassesalarialetypContrat(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|


### Return type

**string**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **stattypContrat**
> string stattypContrat()


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let id: number; // (optional) (default to undefined)

const { status, data } = await apiInstance.stattypContrat(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|


### Return type

**string**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updatePersonnel**
> PersonnelDTO updatePersonnel()


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let id: number; // (optional) (default to undefined)
let nationalite: number; // (optional) (default to undefined)
let nom: string; // (optional) (default to undefined)
let prenom: string; // (optional) (default to undefined)
let service: number; // (optional) (default to undefined)
let matricule: string; // (optional) (default to undefined)
let sexe: string; // (optional) (default to undefined)
let datenaissance: string; // (optional) (default to undefined)
let lieunaissance: string; // (optional) (default to undefined)
let email: string; // (optional) (default to undefined)
let residence: string; // (optional) (default to undefined)
let situationmatrimoniale: number; // (optional) (default to undefined)
let nombreenfant: number; // (optional) (default to undefined)
let datearrivee: string; // (optional) (default to undefined)
let numerocnps: string; // (optional) (default to undefined)
let adresse: string; // (optional) (default to undefined)
let modepaiement: string; // (optional) (default to undefined)
let banque: number; // (optional) (default to undefined)
let numerocompte: string; // (optional) (default to undefined)
let numeroguichet: string; // (optional) (default to undefined)
let rib: string; // (optional) (default to undefined)
let statut: boolean; // (optional) (default to undefined)
let carec: boolean; // (optional) (default to undefined)
let typeEmp: string; // (optional) (default to undefined)
let telephone: string; // (optional) (default to undefined)
let situationMedaille: number; // (optional) (default to undefined)
let situationEmploie: number; // (optional) (default to undefined)
let dateRetourcg: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.updatePersonnel(
    id,
    nationalite,
    nom,
    prenom,
    service,
    matricule,
    sexe,
    datenaissance,
    lieunaissance,
    email,
    residence,
    situationmatrimoniale,
    nombreenfant,
    datearrivee,
    numerocnps,
    adresse,
    modepaiement,
    banque,
    numerocompte,
    numeroguichet,
    rib,
    statut,
    carec,
    typeEmp,
    telephone,
    situationMedaille,
    situationEmploie,
    dateRetourcg
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|
| **nationalite** | [**number**] |  | (optional) defaults to undefined|
| **nom** | [**string**] |  | (optional) defaults to undefined|
| **prenom** | [**string**] |  | (optional) defaults to undefined|
| **service** | [**number**] |  | (optional) defaults to undefined|
| **matricule** | [**string**] |  | (optional) defaults to undefined|
| **sexe** | [**string**] |  | (optional) defaults to undefined|
| **datenaissance** | [**string**] |  | (optional) defaults to undefined|
| **lieunaissance** | [**string**] |  | (optional) defaults to undefined|
| **email** | [**string**] |  | (optional) defaults to undefined|
| **residence** | [**string**] |  | (optional) defaults to undefined|
| **situationmatrimoniale** | [**number**] |  | (optional) defaults to undefined|
| **nombreenfant** | [**number**] |  | (optional) defaults to undefined|
| **datearrivee** | [**string**] |  | (optional) defaults to undefined|
| **numerocnps** | [**string**] |  | (optional) defaults to undefined|
| **adresse** | [**string**] |  | (optional) defaults to undefined|
| **modepaiement** | [**string**] |  | (optional) defaults to undefined|
| **banque** | [**number**] |  | (optional) defaults to undefined|
| **numerocompte** | [**string**] |  | (optional) defaults to undefined|
| **numeroguichet** | [**string**] |  | (optional) defaults to undefined|
| **rib** | [**string**] |  | (optional) defaults to undefined|
| **statut** | [**boolean**] |  | (optional) defaults to undefined|
| **carec** | [**boolean**] |  | (optional) defaults to undefined|
| **typeEmp** | [**string**] |  | (optional) defaults to undefined|
| **telephone** | [**string**] |  | (optional) defaults to undefined|
| **situationMedaille** | [**number**] |  | (optional) defaults to undefined|
| **situationEmploie** | [**number**] |  | (optional) defaults to undefined|
| **dateRetourcg** | [**string**] |  | (optional) defaults to undefined|


### Return type

**PersonnelDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updatePersonnel1**
> PersonnelDTO updatePersonnel1()


### Example

```typescript
import {
    PersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnelControllerApi(configuration);

let idPersonnel: number; // (optional) (default to undefined)
let situationmatrimoniale: number; // (optional) (default to undefined)
let nombreenfant: number; // (optional) (default to undefined)
let statut: boolean; // (optional) (default to undefined)

const { status, data } = await apiInstance.updatePersonnel1(
    idPersonnel,
    situationmatrimoniale,
    nombreenfant,
    statut
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idPersonnel** | [**number**] |  | (optional) defaults to undefined|
| **situationmatrimoniale** | [**number**] |  | (optional) defaults to undefined|
| **nombreenfant** | [**number**] |  | (optional) defaults to undefined|
| **statut** | [**boolean**] |  | (optional) defaults to undefined|


### Return type

**PersonnelDTO**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

