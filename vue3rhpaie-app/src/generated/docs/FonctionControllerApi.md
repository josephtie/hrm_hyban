# FonctionControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteCategory1**](#deletecategory1) | **POST** /api/personnels/supprimerfonction | |
|[**getCategoryListJSON**](#getcategorylistjson) | **POST** /api/personnels/listfonctionjson | |
|[**getFonctionList**](#getfonctionlist) | **GET** /api/personnels/listfonction | |
|[**saveCategory**](#savecategory) | **POST** /api/personnels/enregisterfonction | |
|[**viewFonction**](#viewfonction) | **GET** /api/personnels/fonction | |
|[**viewFonction1**](#viewfonction1) | **HEAD** /api/personnels/fonction | |
|[**viewFonction2**](#viewfonction2) | **POST** /api/personnels/fonction | |
|[**viewFonction3**](#viewfonction3) | **PUT** /api/personnels/fonction | |
|[**viewFonction4**](#viewfonction4) | **PATCH** /api/personnels/fonction | |
|[**viewFonction5**](#viewfonction5) | **DELETE** /api/personnels/fonction | |
|[**viewFonction6**](#viewfonction6) | **OPTIONS** /api/personnels/fonction | |

# **deleteCategory1**
> FonctionDTO deleteCategory1(idRequest)


### Example

```typescript
import {
    FonctionControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FonctionControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.deleteCategory1(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**FonctionDTO**

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

# **getCategoryListJSON**
> FonctionDTO getCategoryListJSON(paginationRequest)


### Example

```typescript
import {
    FonctionControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FonctionControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getCategoryListJSON(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**FonctionDTO**

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

# **getFonctionList**
> Array<Fonction> getFonctionList()


### Example

```typescript
import {
    FonctionControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FonctionControllerApi(configuration);

const { status, data } = await apiInstance.getFonctionList();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<Fonction>**

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

# **saveCategory**
> FonctionDTO saveCategory(idRequest)


### Example

```typescript
import {
    FonctionControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FonctionControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.saveCategory(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**FonctionDTO**

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

# **viewFonction**
> object viewFonction()


### Example

```typescript
import {
    FonctionControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FonctionControllerApi(configuration);

const { status, data } = await apiInstance.viewFonction();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**object**

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

# **viewFonction1**
> object viewFonction1()


### Example

```typescript
import {
    FonctionControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FonctionControllerApi(configuration);

const { status, data } = await apiInstance.viewFonction1();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**object**

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

# **viewFonction2**
> object viewFonction2()


### Example

```typescript
import {
    FonctionControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FonctionControllerApi(configuration);

const { status, data } = await apiInstance.viewFonction2();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**object**

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

# **viewFonction3**
> object viewFonction3()


### Example

```typescript
import {
    FonctionControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FonctionControllerApi(configuration);

const { status, data } = await apiInstance.viewFonction3();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**object**

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

# **viewFonction4**
> object viewFonction4()


### Example

```typescript
import {
    FonctionControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FonctionControllerApi(configuration);

const { status, data } = await apiInstance.viewFonction4();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**object**

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

# **viewFonction5**
> object viewFonction5()


### Example

```typescript
import {
    FonctionControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FonctionControllerApi(configuration);

const { status, data } = await apiInstance.viewFonction5();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**object**

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

# **viewFonction6**
> object viewFonction6()


### Example

```typescript
import {
    FonctionControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FonctionControllerApi(configuration);

const { status, data } = await apiInstance.viewFonction6();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**object**

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

