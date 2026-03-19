# ConjointControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteConjoint**](#deleteconjoint) | **POST** /api/personnel/supprimerconjoint | |
|[**disableConjoint**](#disableconjoint) | **POST** /api/personnel/desactiverconjoint | |
|[**enableConjoint**](#enableconjoint) | **POST** /api/personnel/activerconjoint | |
|[**findConjoints**](#findconjoints) | **POST** /api/personnel/listerconjoints | |
|[**findConjointsByPersonnel**](#findconjointsbypersonnel) | **POST** /api/personnel/listerconjointsparpersonnel | |
|[**findEnfant2**](#findenfant2) | **POST** /api/personnel/trouverconjoint | |
|[**getConjointListJSON**](#getconjointlistjson) | **POST** /api/personnel/paginerconjoints | |
|[**saveConjoint**](#saveconjoint) | **POST** /api/personnel/enregistrerconjoint | |
|[**viewEnfant10**](#viewenfant10) | **POST** /api/personnel/conjoints | |
|[**viewEnfant11**](#viewenfant11) | **PUT** /api/personnel/conjoints | |
|[**viewEnfant12**](#viewenfant12) | **PATCH** /api/personnel/conjoints | |
|[**viewEnfant13**](#viewenfant13) | **DELETE** /api/personnel/conjoints | |
|[**viewEnfant14**](#viewenfant14) | **OPTIONS** /api/personnel/conjoints | |
|[**viewEnfant8**](#viewenfant8) | **GET** /api/personnel/conjoints | |
|[**viewEnfant9**](#viewenfant9) | **HEAD** /api/personnel/conjoints | |

# **deleteConjoint**
> ConjointDTO deleteConjoint(idRequest)


### Example

```typescript
import {
    ConjointControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ConjointControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.deleteConjoint(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**ConjointDTO**

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

# **disableConjoint**
> ConjointDTO disableConjoint(idRequest)


### Example

```typescript
import {
    ConjointControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ConjointControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.disableConjoint(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**ConjointDTO**

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

# **enableConjoint**
> ConjointDTO enableConjoint(idRequest)


### Example

```typescript
import {
    ConjointControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ConjointControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.enableConjoint(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**ConjointDTO**

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

# **findConjoints**
> ConjointDTO findConjoints()


### Example

```typescript
import {
    ConjointControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ConjointControllerApi(configuration);

const { status, data } = await apiInstance.findConjoints();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**ConjointDTO**

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

# **findConjointsByPersonnel**
> ConjointDTO findConjointsByPersonnel(idRequest)


### Example

```typescript
import {
    ConjointControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ConjointControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.findConjointsByPersonnel(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**ConjointDTO**

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

# **findEnfant2**
> ConjointDTO findEnfant2(idRequest)


### Example

```typescript
import {
    ConjointControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ConjointControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.findEnfant2(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**ConjointDTO**

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

# **getConjointListJSON**
> ConjointDTO getConjointListJSON(paginationRequest)


### Example

```typescript
import {
    ConjointControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ConjointControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getConjointListJSON(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**ConjointDTO**

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

# **saveConjoint**
> ConjointDTO saveConjoint(enfantRequest)


### Example

```typescript
import {
    ConjointControllerApi,
    Configuration,
    EnfantRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ConjointControllerApi(configuration);

let enfantRequest: EnfantRequest; //

const { status, data } = await apiInstance.saveConjoint(
    enfantRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **enfantRequest** | **EnfantRequest**|  | |


### Return type

**ConjointDTO**

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

# **viewEnfant10**
> object viewEnfant10()


### Example

```typescript
import {
    ConjointControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ConjointControllerApi(configuration);

const { status, data } = await apiInstance.viewEnfant10();
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

# **viewEnfant11**
> object viewEnfant11()


### Example

```typescript
import {
    ConjointControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ConjointControllerApi(configuration);

const { status, data } = await apiInstance.viewEnfant11();
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

# **viewEnfant12**
> object viewEnfant12()


### Example

```typescript
import {
    ConjointControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ConjointControllerApi(configuration);

const { status, data } = await apiInstance.viewEnfant12();
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

# **viewEnfant13**
> object viewEnfant13()


### Example

```typescript
import {
    ConjointControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ConjointControllerApi(configuration);

const { status, data } = await apiInstance.viewEnfant13();
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

# **viewEnfant14**
> object viewEnfant14()


### Example

```typescript
import {
    ConjointControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ConjointControllerApi(configuration);

const { status, data } = await apiInstance.viewEnfant14();
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

# **viewEnfant8**
> object viewEnfant8()


### Example

```typescript
import {
    ConjointControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ConjointControllerApi(configuration);

const { status, data } = await apiInstance.viewEnfant8();
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

# **viewEnfant9**
> object viewEnfant9()


### Example

```typescript
import {
    ConjointControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ConjointControllerApi(configuration);

const { status, data } = await apiInstance.viewEnfant9();
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

