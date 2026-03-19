# PretPersonnelRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deletePretIndividuel**](#deletepretindividuel) | **DELETE** /api/paie/pret-personnel/individuel/{idp} | |
|[**getPeriodes3**](#getperiodes3) | **GET** /api/paie/pret-personnel/periodes | |
|[**getPretIndividuel**](#getpretindividuel) | **GET** /api/paie/pret-personnel/individuel/{idp} | |
|[**getPretPersonnelList**](#getpretpersonnellist) | **GET** /api/paie/pret-personnel/list | |
|[**getPrets1**](#getprets1) | **GET** /api/paie/pret-personnel/prets | |
|[**savePretPersonnel**](#savepretpersonnel) | **POST** /api/paie/pret-personnel/enregistrer | |
|[**searchPretIndividuel**](#searchpretindividuel) | **GET** /api/paie/pret-personnel/search/{idp} | |
|[**simulerCalcul**](#simulercalcul) | **GET** /api/paie/pret-personnel/simulation-calcul | |
|[**updatePretPersonnel**](#updatepretpersonnel) | **GET** /api/paie/pret-personnel/modifier | |

# **deletePretIndividuel**
> PretPersonnelResponseObject deletePretIndividuel()


### Example

```typescript
import {
    PretPersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PretPersonnelRestControllerApi(configuration);

let idp: number; // (default to undefined)

const { status, data } = await apiInstance.deletePretIndividuel(
    idp
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idp** | [**number**] |  | defaults to undefined|


### Return type

**PretPersonnelResponseObject**

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

# **getPeriodes3**
> PeriodePaieDTO getPeriodes3()


### Example

```typescript
import {
    PretPersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PretPersonnelRestControllerApi(configuration);

const { status, data } = await apiInstance.getPeriodes3();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**PeriodePaieDTO**

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

# **getPretIndividuel**
> PretPersonnelDTO getPretIndividuel()


### Example

```typescript
import {
    PretPersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PretPersonnelRestControllerApi(configuration);

let idp: number; // (default to undefined)

const { status, data } = await apiInstance.getPretIndividuel(
    idp
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idp** | [**number**] |  | defaults to undefined|


### Return type

**PretPersonnelDTO**

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

# **getPretPersonnelList**
> PretPersonnelResponseObject getPretPersonnelList()


### Example

```typescript
import {
    PretPersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PretPersonnelRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getPretPersonnelList(
    limit,
    offset,
    search
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **limit** | [**number**] |  | (optional) defaults to 10|
| **offset** | [**number**] |  | (optional) defaults to 0|
| **search** | [**string**] |  | (optional) defaults to undefined|


### Return type

**PretPersonnelResponseObject**

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

# **getPrets1**
> PretDTO getPrets1()


### Example

```typescript
import {
    PretPersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PretPersonnelRestControllerApi(configuration);

const { status, data } = await apiInstance.getPrets1();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**PretDTO**

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

# **savePretPersonnel**
> PretPersonnelDTO savePretPersonnel(pretPersonnelRequest)


### Example

```typescript
import {
    PretPersonnelRestControllerApi,
    Configuration,
    PretPersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PretPersonnelRestControllerApi(configuration);

let pretPersonnelRequest: PretPersonnelRequest; //

const { status, data } = await apiInstance.savePretPersonnel(
    pretPersonnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **pretPersonnelRequest** | **PretPersonnelRequest**|  | |


### Return type

**PretPersonnelDTO**

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

# **searchPretIndividuel**
> PretPersonnelDTO searchPretIndividuel()


### Example

```typescript
import {
    PretPersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PretPersonnelRestControllerApi(configuration);

let idp: number; // (default to undefined)

const { status, data } = await apiInstance.searchPretIndividuel(
    idp
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idp** | [**number**] |  | defaults to undefined|


### Return type

**PretPersonnelDTO**

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

# **simulerCalcul**
> LivreDePaieSimulationDTO simulerCalcul()


### Example

```typescript
import {
    PretPersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PretPersonnelRestControllerApi(configuration);

let categorie: number; // (default to undefined)
let montantTransp: number; // (default to undefined)
let situationmatrimoniale: number; // (default to undefined)
let nombreenfant: number; // (default to undefined)
let salaireNet: number; // (default to undefined)

const { status, data } = await apiInstance.simulerCalcul(
    categorie,
    montantTransp,
    situationmatrimoniale,
    nombreenfant,
    salaireNet
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **categorie** | [**number**] |  | defaults to undefined|
| **montantTransp** | [**number**] |  | defaults to undefined|
| **situationmatrimoniale** | [**number**] |  | defaults to undefined|
| **nombreenfant** | [**number**] |  | defaults to undefined|
| **salaireNet** | [**number**] |  | defaults to undefined|


### Return type

**LivreDePaieSimulationDTO**

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

# **updatePretPersonnel**
> PretPersonnelDTO updatePretPersonnel()


### Example

```typescript
import {
    PretPersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PretPersonnelRestControllerApi(configuration);

let idpret: number; // (default to undefined)
let montant1: number; // (default to undefined)
let echelonage1: number; // (default to undefined)
let pret1: number; // (default to undefined)
let idpers1: number; // (default to undefined)
let dEmprunt1: string; // (default to undefined)
let periodepaie1: number; // (default to undefined)

const { status, data } = await apiInstance.updatePretPersonnel(
    idpret,
    montant1,
    echelonage1,
    pret1,
    idpers1,
    dEmprunt1,
    periodepaie1
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idpret** | [**number**] |  | defaults to undefined|
| **montant1** | [**number**] |  | defaults to undefined|
| **echelonage1** | [**number**] |  | defaults to undefined|
| **pret1** | [**number**] |  | defaults to undefined|
| **idpers1** | [**number**] |  | defaults to undefined|
| **dEmprunt1** | [**string**] |  | defaults to undefined|
| **periodepaie1** | [**number**] |  | defaults to undefined|


### Return type

**PretPersonnelDTO**

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

