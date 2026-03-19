# EnfantControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteEnfant**](#deleteenfant) | **POST** /api/personnel/supprimerenfant | |
|[**findEnfant1**](#findenfant1) | **POST** /api/personnel/trouverenfant | |
|[**findEnfants**](#findenfants) | **POST** /api/personnel/listerenfants | |
|[**findEnfantsByPersonnel**](#findenfantsbypersonnel) | **POST** /api/personnel/listerenfantsparpersonnel | |
|[**getUserListJSON6**](#getuserlistjson6) | **POST** /api/personnel/paginerenfants | |
|[**saveEnfant**](#saveenfant) | **POST** /api/personnel/enregistrerenfant | |
|[**viewEnfant**](#viewenfant) | **GET** /api/personnel/enfants/init | |

# **deleteEnfant**
> EnfantDTO deleteEnfant(idRequest)


### Example

```typescript
import {
    EnfantControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new EnfantControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.deleteEnfant(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**EnfantDTO**

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

# **findEnfant1**
> EnfantDTO findEnfant1(idRequest)


### Example

```typescript
import {
    EnfantControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new EnfantControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.findEnfant1(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**EnfantDTO**

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

# **findEnfants**
> EnfantDTO findEnfants()


### Example

```typescript
import {
    EnfantControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new EnfantControllerApi(configuration);

const { status, data } = await apiInstance.findEnfants();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**EnfantDTO**

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

# **findEnfantsByPersonnel**
> EnfantDTO findEnfantsByPersonnel(idRequest)


### Example

```typescript
import {
    EnfantControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new EnfantControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.findEnfantsByPersonnel(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**EnfantDTO**

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

# **getUserListJSON6**
> EnfantDTO getUserListJSON6(paginationRequest)


### Example

```typescript
import {
    EnfantControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new EnfantControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getUserListJSON6(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**EnfantDTO**

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

# **saveEnfant**
> EnfantDTO saveEnfant(enfantRequest)


### Example

```typescript
import {
    EnfantControllerApi,
    Configuration,
    EnfantRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new EnfantControllerApi(configuration);

let enfantRequest: EnfantRequest; //

const { status, data } = await apiInstance.saveEnfant(
    enfantRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **enfantRequest** | **EnfantRequest**|  | |


### Return type

**EnfantDTO**

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

# **viewEnfant**
> object viewEnfant()


### Example

```typescript
import {
    EnfantControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new EnfantControllerApi(configuration);

const { status, data } = await apiInstance.viewEnfant();
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

