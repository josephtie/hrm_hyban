# FormateurControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteFormateur**](#deleteformateur) | **POST** /formation/supprimerformateur | |
|[**findFormateur**](#findformateur) | **POST** /formation/trouverformateur | |
|[**findFormateurs**](#findformateurs) | **POST** /formation/listerformateurs | |
|[**getUserListJSON2**](#getuserlistjson2) | **POST** /formation/paginerformateurs | |
|[**saveFormateur**](#saveformateur) | **POST** /formation/enregistrerformateur | |

# **deleteFormateur**
> FormateurDTO deleteFormateur(idRequest)


### Example

```typescript
import {
    FormateurControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FormateurControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.deleteFormateur(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**FormateurDTO**

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

# **findFormateur**
> FormateurDTO findFormateur(idRequest)


### Example

```typescript
import {
    FormateurControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FormateurControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.findFormateur(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**FormateurDTO**

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

# **findFormateurs**
> FormateurDTO findFormateurs()


### Example

```typescript
import {
    FormateurControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FormateurControllerApi(configuration);

const { status, data } = await apiInstance.findFormateurs();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**FormateurDTO**

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

# **getUserListJSON2**
> FormateurDTO getUserListJSON2(paginationRequest)


### Example

```typescript
import {
    FormateurControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FormateurControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getUserListJSON2(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**FormateurDTO**

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

# **saveFormateur**
> FormateurDTO saveFormateur(formateurRequest)


### Example

```typescript
import {
    FormateurControllerApi,
    Configuration,
    FormateurRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FormateurControllerApi(configuration);

let formateurRequest: FormateurRequest; //

const { status, data } = await apiInstance.saveFormateur(
    formateurRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **formateurRequest** | **FormateurRequest**|  | |


### Return type

**FormateurDTO**

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

