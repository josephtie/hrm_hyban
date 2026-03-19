# FactureFormationControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteFactureFormation**](#deletefactureformation) | **POST** /formation/supprimerfactureformation | |
|[**findFactureFormation**](#findfactureformation) | **POST** /formation/trouverfactureformation | |
|[**findFactureFormations**](#findfactureformations) | **POST** /formation/listerfactureformations | |
|[**findFactureFormationsByFormation**](#findfactureformationsbyformation) | **POST** /formation/listerfactureformationsparformation | |
|[**getUserListJSON3**](#getuserlistjson3) | **POST** /formation/paginerfactureformations | |
|[**saveFactureFormation**](#savefactureformation) | **POST** /formation/enregistrerfactureformation | |

# **deleteFactureFormation**
> FactureFormationDTO deleteFactureFormation(idRequest)


### Example

```typescript
import {
    FactureFormationControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FactureFormationControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.deleteFactureFormation(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**FactureFormationDTO**

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

# **findFactureFormation**
> FactureFormationDTO findFactureFormation(idRequest)


### Example

```typescript
import {
    FactureFormationControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FactureFormationControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.findFactureFormation(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**FactureFormationDTO**

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

# **findFactureFormations**
> FactureFormationDTO findFactureFormations()


### Example

```typescript
import {
    FactureFormationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FactureFormationControllerApi(configuration);

const { status, data } = await apiInstance.findFactureFormations();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**FactureFormationDTO**

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

# **findFactureFormationsByFormation**
> FactureFormationDTO findFactureFormationsByFormation(idRequest)


### Example

```typescript
import {
    FactureFormationControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FactureFormationControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.findFactureFormationsByFormation(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**FactureFormationDTO**

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

# **getUserListJSON3**
> FactureFormationDTO getUserListJSON3(paginationRequest)


### Example

```typescript
import {
    FactureFormationControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FactureFormationControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getUserListJSON3(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**FactureFormationDTO**

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

# **saveFactureFormation**
> FactureFormationDTO saveFactureFormation(factureFormationRequest)


### Example

```typescript
import {
    FactureFormationControllerApi,
    Configuration,
    FactureFormationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new FactureFormationControllerApi(configuration);

let factureFormationRequest: FactureFormationRequest; //

const { status, data } = await apiInstance.saveFactureFormation(
    factureFormationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **factureFormationRequest** | **FactureFormationRequest**|  | |


### Return type

**FactureFormationDTO**

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

