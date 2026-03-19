# GratificationRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**chargerGratificationParPeriode**](#chargergratificationparperiode) | **POST** /api/gratification/charger-par-periode | |
|[**genererGratification**](#generergratification) | **POST** /api/gratification/generer | |
|[**getBanques1**](#getbanques1) | **GET** /api/gratification/banques | |
|[**getBanquesEntreprise**](#getbanquesentreprise) | **GET** /api/gratification/banques-entreprise | |
|[**getGratificationList**](#getgratificationlist) | **GET** /api/gratification/list | |
|[**getPeriodesOuvertes**](#getperiodesouvertes) | **GET** /api/gratification/periodes-ouvertes | |
|[**imprimerBulletinGratification**](#imprimerbulletingratification) | **GET** /api/gratification/bulletin/{id} | |
|[**imprimerOrdreVirement**](#imprimerordrevirement) | **POST** /api/gratification/virement | |

# **chargerGratificationParPeriode**
> Array<Gratification> chargerGratificationParPeriode()


### Example

```typescript
import {
    GratificationRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new GratificationRestControllerApi(configuration);

let periodepaie: number; // (default to undefined)

const { status, data } = await apiInstance.chargerGratificationParPeriode(
    periodepaie
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **periodepaie** | [**number**] |  | defaults to undefined|


### Return type

**Array<Gratification>**

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

# **genererGratification**
> GratificationDTO genererGratification()


### Example

```typescript
import {
    GratificationRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new GratificationRestControllerApi(configuration);

const { status, data } = await apiInstance.genererGratification();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**GratificationDTO**

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

# **getBanques1**
> Array<Banque> getBanques1()


### Example

```typescript
import {
    GratificationRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new GratificationRestControllerApi(configuration);

const { status, data } = await apiInstance.getBanques1();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<Banque>**

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

# **getBanquesEntreprise**
> Array<Banque> getBanquesEntreprise()


### Example

```typescript
import {
    GratificationRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new GratificationRestControllerApi(configuration);

const { status, data } = await apiInstance.getBanquesEntreprise();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<Banque>**

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

# **getGratificationList**
> GratificationResponseGratification getGratificationList()


### Example

```typescript
import {
    GratificationRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new GratificationRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getGratificationList(
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

**GratificationResponseGratification**

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

# **getPeriodesOuvertes**
> Array<PeriodePaie> getPeriodesOuvertes()


### Example

```typescript
import {
    GratificationRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new GratificationRestControllerApi(configuration);

const { status, data } = await apiInstance.getPeriodesOuvertes();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<PeriodePaie>**

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

# **imprimerBulletinGratification**
> string imprimerBulletinGratification()


### Example

```typescript
import {
    GratificationRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new GratificationRestControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.imprimerBulletinGratification(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


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

# **imprimerOrdreVirement**
> BulletinPaieDTO imprimerOrdreVirement(gratificationRequest)


### Example

```typescript
import {
    GratificationRestControllerApi,
    Configuration,
    GratificationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new GratificationRestControllerApi(configuration);

let gratificationRequest: GratificationRequest; //

const { status, data } = await apiInstance.imprimerOrdreVirement(
    gratificationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **gratificationRequest** | **GratificationRequest**|  | |


### Return type

**BulletinPaieDTO**

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

