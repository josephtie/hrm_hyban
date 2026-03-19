# BulletinPaieRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**calculerSursalaire**](#calculersursalaire) | **POST** /api/paie/calcul-sursalaire | |
|[**calculerSursalaireListe**](#calculersursalaireliste) | **POST** /api/paie/calcul-sursalaire/liste | |
|[**chargerBulletinsParPeriode**](#chargerbulletinsparperiode) | **POST** /api/paie/bulletins/search | |
|[**generateLivrePaie**](#generatelivrepaie) | **GET** /api/paie/livre-paie/generate | |
|[**getBulletinsByPeriode**](#getbulletinsbyperiode) | **GET** /api/paie/bulletins/periode/{idPeriod} | |
|[**getBulletinsMoisActif**](#getbulletinsmoisactif) | **GET** /api/paie/bulletins/mois-actif | |
|[**getPersonnelPeriodeActif**](#getpersonnelperiodeactif) | **GET** /api/paie/personnel/periode-actif | |

# **calculerSursalaire**
> LivreDePaie calculerSursalaire(calculSursalaireRequest)


### Example

```typescript
import {
    BulletinPaieRestControllerApi,
    Configuration,
    CalculSursalaireRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new BulletinPaieRestControllerApi(configuration);

let calculSursalaireRequest: CalculSursalaireRequest; //

const { status, data } = await apiInstance.calculerSursalaire(
    calculSursalaireRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **calculSursalaireRequest** | **CalculSursalaireRequest**|  | |


### Return type

**LivreDePaie**

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

# **calculerSursalaireListe**
> BulletinPaieResponseLivreDePaie calculerSursalaireListe()


### Example

```typescript
import {
    BulletinPaieRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BulletinPaieRestControllerApi(configuration);

const { status, data } = await apiInstance.calculerSursalaireListe();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**BulletinPaieResponseLivreDePaie**

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

# **chargerBulletinsParPeriode**
> BulletinPaieResponseBulletinPaie chargerBulletinsParPeriode(bulletinPaieRequest)


### Example

```typescript
import {
    BulletinPaieRestControllerApi,
    Configuration,
    BulletinPaieRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new BulletinPaieRestControllerApi(configuration);

let bulletinPaieRequest: BulletinPaieRequest; //

const { status, data } = await apiInstance.chargerBulletinsParPeriode(
    bulletinPaieRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **bulletinPaieRequest** | **BulletinPaieRequest**|  | |


### Return type

**BulletinPaieResponseBulletinPaie**

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

# **generateLivrePaie**
> BulletinPaieResponseLivreDePaie generateLivrePaie()


### Example

```typescript
import {
    BulletinPaieRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BulletinPaieRestControllerApi(configuration);

let id: number; // (optional) (default to undefined)
let limit: number; // (optional) (default to 100)
let offset: number; // (optional) (default to 0)

const { status, data } = await apiInstance.generateLivrePaie(
    id,
    limit,
    offset
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | (optional) defaults to undefined|
| **limit** | [**number**] |  | (optional) defaults to 100|
| **offset** | [**number**] |  | (optional) defaults to 0|


### Return type

**BulletinPaieResponseLivreDePaie**

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

# **getBulletinsByPeriode**
> BulletinPaieResponseBulletinPaie getBulletinsByPeriode()


### Example

```typescript
import {
    BulletinPaieRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BulletinPaieRestControllerApi(configuration);

let idPeriod: number; // (default to undefined)
let limit: number; // (optional) (default to 50)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getBulletinsByPeriode(
    idPeriod,
    limit,
    offset,
    search
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idPeriod** | [**number**] |  | defaults to undefined|
| **limit** | [**number**] |  | (optional) defaults to 50|
| **offset** | [**number**] |  | (optional) defaults to 0|
| **search** | [**string**] |  | (optional) defaults to undefined|


### Return type

**BulletinPaieResponseBulletinPaie**

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

# **getBulletinsMoisActif**
> BulletinPaieResponseBulletinPaie getBulletinsMoisActif()


### Example

```typescript
import {
    BulletinPaieRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BulletinPaieRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getBulletinsMoisActif(
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

**BulletinPaieResponseBulletinPaie**

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

# **getPersonnelPeriodeActif**
> Array<ContratPersonnel> getPersonnelPeriodeActif()


### Example

```typescript
import {
    BulletinPaieRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BulletinPaieRestControllerApi(configuration);

const { status, data } = await apiInstance.getPersonnelPeriodeActif();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<ContratPersonnel>**

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

