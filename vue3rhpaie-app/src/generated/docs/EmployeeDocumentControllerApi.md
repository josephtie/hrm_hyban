# EmployeeDocumentControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**all1**](#all1) | **GET** /api/personnel/documents | |
|[**byEmployee**](#byemployee) | **POST** /api/personnel/documents/employeId | |
|[**create3**](#create3) | **POST** /api/personnel/documents | |
|[**deleteDocument**](#deletedocument) | **POST** /api/personnel/documents/delete | |
|[**downloadDocument**](#downloaddocument) | **POST** /api/personnel/documents/download | |
|[**uploadDocument**](#uploaddocument) | **POST** /api/personnel/documents/upload | |

# **all1**
> Array<EmployeeDocument> all1()


### Example

```typescript
import {
    EmployeeDocumentControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new EmployeeDocumentControllerApi(configuration);

const { status, data } = await apiInstance.all1();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<EmployeeDocument>**

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

# **byEmployee**
> EmployeeDocumentDTO byEmployee(idRequest)


### Example

```typescript
import {
    EmployeeDocumentControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new EmployeeDocumentControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.byEmployee(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**EmployeeDocumentDTO**

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

# **create3**
> EmployeeDocument create3(employeeDocument)


### Example

```typescript
import {
    EmployeeDocumentControllerApi,
    Configuration,
    EmployeeDocument
} from './api';

const configuration = new Configuration();
const apiInstance = new EmployeeDocumentControllerApi(configuration);

let employeeDocument: EmployeeDocument; //

const { status, data } = await apiInstance.create3(
    employeeDocument
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **employeeDocument** | **EmployeeDocument**|  | |


### Return type

**EmployeeDocument**

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

# **deleteDocument**
> EmployeeDocumentDTO deleteDocument(idRequest)


### Example

```typescript
import {
    EmployeeDocumentControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new EmployeeDocumentControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.deleteDocument(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**EmployeeDocumentDTO**

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

# **downloadDocument**
> File downloadDocument(idRequest)


### Example

```typescript
import {
    EmployeeDocumentControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new EmployeeDocumentControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.downloadDocument(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**File**

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

# **uploadDocument**
> EmployeeDocumentDTO uploadDocument()


### Example

```typescript
import {
    EmployeeDocumentControllerApi,
    Configuration,
    UploadDocumentRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new EmployeeDocumentControllerApi(configuration);

let idPersonnel: number; // (default to undefined)
let idDocument: number; // (default to undefined)
let dateDepot: string; // (default to undefined)
let statutpresent: boolean; // (default to undefined)
let numeroReference: string; // (default to undefined)
let idStorage: number; // (default to undefined)
let description: string; // (optional) (default to undefined)
let uploadDocumentRequest: UploadDocumentRequest; // (optional)

const { status, data } = await apiInstance.uploadDocument(
    idPersonnel,
    idDocument,
    dateDepot,
    statutpresent,
    numeroReference,
    idStorage,
    description,
    uploadDocumentRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uploadDocumentRequest** | **UploadDocumentRequest**|  | |
| **idPersonnel** | [**number**] |  | defaults to undefined|
| **idDocument** | [**number**] |  | defaults to undefined|
| **dateDepot** | [**string**] |  | defaults to undefined|
| **statutpresent** | [**boolean**] |  | defaults to undefined|
| **numeroReference** | [**string**] |  | defaults to undefined|
| **idStorage** | [**number**] |  | defaults to undefined|
| **description** | [**string**] |  | (optional) defaults to undefined|


### Return type

**EmployeeDocumentDTO**

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

