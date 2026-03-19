# TypeSanctionRestControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteTypeSanction**](#deletetypesanction) | **POST** /api/rh/carriere/types-sanctions/supprimer | |
|[**findTypeSanction**](#findtypesanction) | **POST** /api/rh/carriere/types-sanctions/trouver | |
|[**findTypeSanctions**](#findtypesanctions) | **POST** /api/rh/carriere/types-sanctions/lister | |
|[**getSocietes**](#getsocietes) | **GET** /api/rh/carriere/types-sanctions/societes | |
|[**getTypeSanctionList**](#gettypesanctionlist) | **GET** /api/rh/carriere/types-sanctions/list | |
|[**saveTypeSanction**](#savetypesanction) | **POST** /api/rh/carriere/types-sanctions/enregistrer | |
|[**viewTypesSanctions**](#viewtypessanctions) | **GET** /api/rh/carriere/types-sanctions/view | |

# **deleteTypeSanction**
> TypeSanctionDTO deleteTypeSanction(typeSanctionRequest)


### Example

```typescript
import {
    TypeSanctionRestControllerApi,
    Configuration,
    TypeSanctionRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeSanctionRestControllerApi(configuration);

let typeSanctionRequest: TypeSanctionRequest; //

const { status, data } = await apiInstance.deleteTypeSanction(
    typeSanctionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **typeSanctionRequest** | **TypeSanctionRequest**|  | |


### Return type

**TypeSanctionDTO**

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

# **findTypeSanction**
> TypeSanctionDTO findTypeSanction(typeSanctionRequest)


### Example

```typescript
import {
    TypeSanctionRestControllerApi,
    Configuration,
    TypeSanctionRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeSanctionRestControllerApi(configuration);

let typeSanctionRequest: TypeSanctionRequest; //

const { status, data } = await apiInstance.findTypeSanction(
    typeSanctionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **typeSanctionRequest** | **TypeSanctionRequest**|  | |


### Return type

**TypeSanctionDTO**

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

# **findTypeSanctions**
> TypeSanctionDTO findTypeSanctions()


### Example

```typescript
import {
    TypeSanctionRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeSanctionRestControllerApi(configuration);

const { status, data } = await apiInstance.findTypeSanctions();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**TypeSanctionDTO**

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

# **getSocietes**
> Array<Societe> getSocietes()


### Example

```typescript
import {
    TypeSanctionRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeSanctionRestControllerApi(configuration);

const { status, data } = await apiInstance.getSocietes();
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

# **getTypeSanctionList**
> TypeSanctionResponseObject getTypeSanctionList()


### Example

```typescript
import {
    TypeSanctionRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeSanctionRestControllerApi(configuration);

let limit: number; // (optional) (default to 10)
let offset: number; // (optional) (default to 0)
let search: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getTypeSanctionList(
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

**TypeSanctionResponseObject**

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

# **saveTypeSanction**
> TypeSanctionDTO saveTypeSanction(typeSanctionRequest)


### Example

```typescript
import {
    TypeSanctionRestControllerApi,
    Configuration,
    TypeSanctionRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeSanctionRestControllerApi(configuration);

let typeSanctionRequest: TypeSanctionRequest; //

const { status, data } = await apiInstance.saveTypeSanction(
    typeSanctionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **typeSanctionRequest** | **TypeSanctionRequest**|  | |


### Return type

**TypeSanctionDTO**

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

# **viewTypesSanctions**
> string viewTypesSanctions()


### Example

```typescript
import {
    TypeSanctionRestControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeSanctionRestControllerApi(configuration);

const { status, data } = await apiInstance.viewTypesSanctions();
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

