# PrimePersonnelRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deletePrimePersonnel**](#deleteprimepersonnel) | **DELETE** /api/paie/prime-personnel/supprimer/{id} | |
|[**getPeriodes2**](#getperiodes2) | **GET** /api/paie/prime-personnel/periodes | |
|[**getPrets**](#getprets) | **GET** /api/paie/prime-personnel/prets | |
|[**getPrimeIndividuel**](#getprimeindividuel) | **GET** /api/paie/prime-personnel/prime-individuel | |
|[**getPrimeMoisEncours**](#getprimemoisencours) | **GET** /api/paie/prime-personnel/prime-mois-encours/{id} | |
|[**getPrimePersonnelList**](#getprimepersonnellist) | **GET** /api/paie/prime-personnel/list | |
|[**rechercherPrimePersonnel**](#rechercherprimepersonnel) | **GET** /api/paie/prime-personnel/rechercher/{id} | |
|[**savePrimePersonnel**](#saveprimepersonnel) | **POST** /api/paie/prime-personnel/enregistrer | |

# **deletePrimePersonnel**
> PrimePersonnelDTO deletePrimePersonnel()


### Example

```typescript
import {
    PrimePersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimePersonnelRestControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.deletePrimePersonnel(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**PrimePersonnelDTO**

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

# **getPeriodes2**
> PeriodePaieDTO getPeriodes2()


### Example

```typescript
import {
    PrimePersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimePersonnelRestControllerApi(configuration);

const { status, data } = await apiInstance.getPeriodes2();
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

# **getPrets**
> PretDTO getPrets()


### Example

```typescript
import {
    PrimePersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimePersonnelRestControllerApi(configuration);

const { status, data } = await apiInstance.getPrets();
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

# **getPrimeIndividuel**
> Array<PrimePersonnel> getPrimeIndividuel()


### Example

```typescript
import {
    PrimePersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimePersonnelRestControllerApi(configuration);

let idPrime: number; // (default to undefined)
let idPeriode: number; // (default to undefined)
let idCtrat: number; // (default to undefined)

const { status, data } = await apiInstance.getPrimeIndividuel(
    idPrime,
    idPeriode,
    idCtrat
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idPrime** | [**number**] |  | defaults to undefined|
| **idPeriode** | [**number**] |  | defaults to undefined|
| **idCtrat** | [**number**] |  | defaults to undefined|


### Return type

**Array<PrimePersonnel>**

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

# **getPrimeMoisEncours**
> PrimePersonnelDTO getPrimeMoisEncours()


### Example

```typescript
import {
    PrimePersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimePersonnelRestControllerApi(configuration);

let id: number; // (default to undefined)
let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getPrimeMoisEncours(
    id,
    limit,
    offset,
    search
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|
| **limit** | [**number**] |  | (optional) defaults to 10|
| **offset** | [**number**] |  | (optional) defaults to 0|
| **search** | [**string**] |  | (optional) defaults to undefined|


### Return type

**PrimePersonnelDTO**

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

# **getPrimePersonnelList**
> PrimePersonnelResponsePrimePersonnel getPrimePersonnelList()


### Example

```typescript
import {
    PrimePersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimePersonnelRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getPrimePersonnelList(
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

**PrimePersonnelResponsePrimePersonnel**

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

# **rechercherPrimePersonnel**
> PrimePersonnel rechercherPrimePersonnel()


### Example

```typescript
import {
    PrimePersonnelRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimePersonnelRestControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.rechercherPrimePersonnel(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**PrimePersonnel**

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

# **savePrimePersonnel**
> PrimePersonnelDTO savePrimePersonnel(primePersonnelRequest)


### Example

```typescript
import {
    PrimePersonnelRestControllerApi,
    Configuration,
    PrimePersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimePersonnelRestControllerApi(configuration);

let primePersonnelRequest: PrimePersonnelRequest; //

const { status, data } = await apiInstance.savePrimePersonnel(
    primePersonnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **primePersonnelRequest** | **PrimePersonnelRequest**|  | |


### Return type

**PrimePersonnelDTO**

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

