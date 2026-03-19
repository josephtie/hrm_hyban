# SoldeTousCompteRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**genererOrdreVirement**](#genererordrevirement) | **POST** /api/depart/solde-tous-comptes/generer-virement | |
|[**getBanques2**](#getbanques2) | **GET** /api/depart/solde-tous-comptes/banques | |
|[**getBanquesEntreprise1**](#getbanquesentreprise1) | **GET** /api/depart/solde-tous-comptes/banques-entreprise | |
|[**getPeriodeActive4**](#getperiodeactive4) | **GET** /api/depart/solde-tous-comptes/periode-active | |
|[**getPeriodesOuvertes1**](#getperiodesouvertes1) | **GET** /api/depart/solde-tous-comptes/periodes-ouvertes | |
|[**getSocietes10**](#getsocietes10) | **GET** /api/depart/solde-tous-comptes/societes | |
|[**viewLivrePaie**](#viewlivrepaie) | **GET** /api/depart/solde-tous-comptes/view | |

# **genererOrdreVirement**
> BulletinPaieDTO genererOrdreVirement(soldeTousCompteRequest)


### Example

```typescript
import {
    SoldeTousCompteRestControllerApi,
    Configuration,
    SoldeTousCompteRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new SoldeTousCompteRestControllerApi(configuration);

let soldeTousCompteRequest: SoldeTousCompteRequest; //

const { status, data } = await apiInstance.genererOrdreVirement(
    soldeTousCompteRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **soldeTousCompteRequest** | **SoldeTousCompteRequest**|  | |


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

# **getBanques2**
> Array<Banque> getBanques2()


### Example

```typescript
import {
    SoldeTousCompteRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SoldeTousCompteRestControllerApi(configuration);

const { status, data } = await apiInstance.getBanques2();
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

# **getBanquesEntreprise1**
> Array<Banque> getBanquesEntreprise1()


### Example

```typescript
import {
    SoldeTousCompteRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SoldeTousCompteRestControllerApi(configuration);

const { status, data } = await apiInstance.getBanquesEntreprise1();
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

# **getPeriodeActive4**
> PeriodePaie getPeriodeActive4()


### Example

```typescript
import {
    SoldeTousCompteRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SoldeTousCompteRestControllerApi(configuration);

const { status, data } = await apiInstance.getPeriodeActive4();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**PeriodePaie**

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

# **getPeriodesOuvertes1**
> Array<PeriodePaie> getPeriodesOuvertes1()


### Example

```typescript
import {
    SoldeTousCompteRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SoldeTousCompteRestControllerApi(configuration);

const { status, data } = await apiInstance.getPeriodesOuvertes1();
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

# **getSocietes10**
> Array<Societe> getSocietes10()


### Example

```typescript
import {
    SoldeTousCompteRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SoldeTousCompteRestControllerApi(configuration);

const { status, data } = await apiInstance.getSocietes10();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<Societe>**

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

# **viewLivrePaie**
> string viewLivrePaie()


### Example

```typescript
import {
    SoldeTousCompteRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new SoldeTousCompteRestControllerApi(configuration);

const { status, data } = await apiInstance.viewLivrePaie();
```

### Parameters
This endpoint does not have any parameters.


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

