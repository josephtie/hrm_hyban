# PrimeSpecifiqueControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**byContrat**](#bycontrat) | **GET** /parametrages/contrat/{id} | |
|[**byPeriode**](#byperiode) | **GET** /parametrages/periode/{id} | |
|[**create**](#create) | **POST** /parametrages | |
|[**viewAccountType1**](#viewaccounttype1) | **GET** /parametrages/speciales | |
|[**viewAccountType2**](#viewaccounttype2) | **HEAD** /parametrages/speciales | |
|[**viewAccountType3**](#viewaccounttype3) | **POST** /parametrages/speciales | |
|[**viewAccountType4**](#viewaccounttype4) | **PUT** /parametrages/speciales | |
|[**viewAccountType5**](#viewaccounttype5) | **PATCH** /parametrages/speciales | |
|[**viewAccountType6**](#viewaccounttype6) | **DELETE** /parametrages/speciales | |
|[**viewAccountType7**](#viewaccounttype7) | **OPTIONS** /parametrages/speciales | |

# **byContrat**
> Array<PrimeSpecifique> byContrat()


### Example

```typescript
import {
    PrimeSpecifiqueControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimeSpecifiqueControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.byContrat(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**Array<PrimeSpecifique>**

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

# **byPeriode**
> Array<PrimeSpecifique> byPeriode()


### Example

```typescript
import {
    PrimeSpecifiqueControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimeSpecifiqueControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.byPeriode(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**Array<PrimeSpecifique>**

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

# **create**
> PrimeSpecifique create(primeSpecifique)


### Example

```typescript
import {
    PrimeSpecifiqueControllerApi,
    Configuration,
    PrimeSpecifique
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimeSpecifiqueControllerApi(configuration);

let primeSpecifique: PrimeSpecifique; //

const { status, data } = await apiInstance.create(
    primeSpecifique
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **primeSpecifique** | **PrimeSpecifique**|  | |


### Return type

**PrimeSpecifique**

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

# **viewAccountType1**
> string viewAccountType1()


### Example

```typescript
import {
    PrimeSpecifiqueControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimeSpecifiqueControllerApi(configuration);

const { status, data } = await apiInstance.viewAccountType1();
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

# **viewAccountType2**
> string viewAccountType2()


### Example

```typescript
import {
    PrimeSpecifiqueControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimeSpecifiqueControllerApi(configuration);

const { status, data } = await apiInstance.viewAccountType2();
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

# **viewAccountType3**
> string viewAccountType3()


### Example

```typescript
import {
    PrimeSpecifiqueControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimeSpecifiqueControllerApi(configuration);

const { status, data } = await apiInstance.viewAccountType3();
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

# **viewAccountType4**
> string viewAccountType4()


### Example

```typescript
import {
    PrimeSpecifiqueControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimeSpecifiqueControllerApi(configuration);

const { status, data } = await apiInstance.viewAccountType4();
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

# **viewAccountType5**
> string viewAccountType5()


### Example

```typescript
import {
    PrimeSpecifiqueControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimeSpecifiqueControllerApi(configuration);

const { status, data } = await apiInstance.viewAccountType5();
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

# **viewAccountType6**
> string viewAccountType6()


### Example

```typescript
import {
    PrimeSpecifiqueControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimeSpecifiqueControllerApi(configuration);

const { status, data } = await apiInstance.viewAccountType6();
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

# **viewAccountType7**
> string viewAccountType7()


### Example

```typescript
import {
    PrimeSpecifiqueControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PrimeSpecifiqueControllerApi(configuration);

const { status, data } = await apiInstance.viewAccountType7();
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

