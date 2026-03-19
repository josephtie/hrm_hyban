# TypeDocumentControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteTypeDocument**](#deletetypedocument) | **POST** /api/parametrages/doc/supprimertypedocument | |
|[**findTypeDocument**](#findtypedocument) | **POST** /api/parametrages/doc/trouvertypedocument | |
|[**findTypesDocuments**](#findtypesdocuments) | **POST** /api/parametrages/doc/listertypesdocuments | |
|[**getUserListJSON7**](#getuserlistjson7) | **POST** /api/parametrages/doc/paginertypesdocuments | |
|[**saveTypeDocument**](#savetypedocument) | **POST** /api/parametrages/doc/enregistrertypedocument | |
|[**viewAccountType10**](#viewaccounttype10) | **POST** /api/parametrages/doc/typedocument | |
|[**viewAccountType11**](#viewaccounttype11) | **PUT** /api/parametrages/doc/typedocument | |
|[**viewAccountType12**](#viewaccounttype12) | **PATCH** /api/parametrages/doc/typedocument | |
|[**viewAccountType13**](#viewaccounttype13) | **DELETE** /api/parametrages/doc/typedocument | |
|[**viewAccountType14**](#viewaccounttype14) | **OPTIONS** /api/parametrages/doc/typedocument | |
|[**viewAccountType8**](#viewaccounttype8) | **GET** /api/parametrages/doc/typedocument | |
|[**viewAccountType9**](#viewaccounttype9) | **HEAD** /api/parametrages/doc/typedocument | |

# **deleteTypeDocument**
> TypeDocumentDTO deleteTypeDocument(idRequest)


### Example

```typescript
import {
    TypeDocumentControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeDocumentControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.deleteTypeDocument(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**TypeDocumentDTO**

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

# **findTypeDocument**
> TypeDocumentDTO findTypeDocument(idRequest)


### Example

```typescript
import {
    TypeDocumentControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeDocumentControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.findTypeDocument(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**TypeDocumentDTO**

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

# **findTypesDocuments**
> TypeDocumentDTO findTypesDocuments()


### Example

```typescript
import {
    TypeDocumentControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeDocumentControllerApi(configuration);

const { status, data } = await apiInstance.findTypesDocuments();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**TypeDocumentDTO**

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

# **getUserListJSON7**
> TypeDocumentDTO getUserListJSON7(paginationRequest)


### Example

```typescript
import {
    TypeDocumentControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeDocumentControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getUserListJSON7(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**TypeDocumentDTO**

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

# **saveTypeDocument**
> TypeDocumentDTO saveTypeDocument(typeDocumentRequest)


### Example

```typescript
import {
    TypeDocumentControllerApi,
    Configuration,
    TypeDocumentRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeDocumentControllerApi(configuration);

let typeDocumentRequest: TypeDocumentRequest; //

const { status, data } = await apiInstance.saveTypeDocument(
    typeDocumentRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **typeDocumentRequest** | **TypeDocumentRequest**|  | |


### Return type

**TypeDocumentDTO**

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

# **viewAccountType10**
> string viewAccountType10()


### Example

```typescript
import {
    TypeDocumentControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeDocumentControllerApi(configuration);

const { status, data } = await apiInstance.viewAccountType10();
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

# **viewAccountType11**
> string viewAccountType11()


### Example

```typescript
import {
    TypeDocumentControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeDocumentControllerApi(configuration);

const { status, data } = await apiInstance.viewAccountType11();
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

# **viewAccountType12**
> string viewAccountType12()


### Example

```typescript
import {
    TypeDocumentControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeDocumentControllerApi(configuration);

const { status, data } = await apiInstance.viewAccountType12();
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

# **viewAccountType13**
> string viewAccountType13()


### Example

```typescript
import {
    TypeDocumentControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeDocumentControllerApi(configuration);

const { status, data } = await apiInstance.viewAccountType13();
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

# **viewAccountType14**
> string viewAccountType14()


### Example

```typescript
import {
    TypeDocumentControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeDocumentControllerApi(configuration);

const { status, data } = await apiInstance.viewAccountType14();
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

# **viewAccountType8**
> string viewAccountType8()


### Example

```typescript
import {
    TypeDocumentControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeDocumentControllerApi(configuration);

const { status, data } = await apiInstance.viewAccountType8();
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

# **viewAccountType9**
> string viewAccountType9()


### Example

```typescript
import {
    TypeDocumentControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TypeDocumentControllerApi(configuration);

const { status, data } = await apiInstance.viewAccountType9();
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

