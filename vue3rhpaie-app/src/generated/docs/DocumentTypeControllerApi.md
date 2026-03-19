# DocumentTypeControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**all2**](#all2) | **GET** /api/personnel/document-types | |
|[**create4**](#create4) | **POST** /api/personnel/document-types | |

# **all2**
> Array<DocumentType> all2()


### Example

```typescript
import {
    DocumentTypeControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DocumentTypeControllerApi(configuration);

const { status, data } = await apiInstance.all2();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<DocumentType>**

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

# **create4**
> DocumentType create4(documentType)


### Example

```typescript
import {
    DocumentTypeControllerApi,
    Configuration,
    DocumentType
} from './api';

const configuration = new Configuration();
const apiInstance = new DocumentTypeControllerApi(configuration);

let documentType: DocumentType; //

const { status, data } = await apiInstance.create4(
    documentType
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **documentType** | **DocumentType**|  | |


### Return type

**DocumentType**

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

