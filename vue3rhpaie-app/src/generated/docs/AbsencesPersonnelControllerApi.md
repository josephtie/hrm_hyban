# AbsencesPersonnelControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deleteAbsencesPersonnel**](#deleteabsencespersonnel) | **POST** /api/absence/supprimerabsencepersonnel | |
|[**findAbsencePersonnelsByAbsence**](#findabsencepersonnelsbyabsence) | **POST** /api/absence/listerabsencepersonnelsparabsence | |
|[**findAbsencePersonnelsByPersonnel**](#findabsencepersonnelsbypersonnel) | **POST** /api/absence/listerabsencepersonnelsparpersonnel | |
|[**findAbsencesPersonnel**](#findabsencespersonnel) | **POST** /api/absence/trouverabsencepersonnel | |
|[**findAbsencesPersonnels**](#findabsencespersonnels) | **POST** /api/absence/listerabsencepersonnels | |
|[**getUserListabsenceJSON**](#getuserlistabsencejson) | **POST** /api/absence/paginerabsencePersonnels | |
|[**saveAbsencesPersonnel**](#saveabsencespersonnel) | **POST** /api/absence/enregistrerabsencepersonnel | |
|[**viewAbsencePersonnel**](#viewabsencepersonnel) | **GET** /api/absence/absencepersonnel | |
|[**viewAbsencePersonnel1**](#viewabsencepersonnel1) | **HEAD** /api/absence/absencepersonnel | |
|[**viewAbsencePersonnel2**](#viewabsencepersonnel2) | **POST** /api/absence/absencepersonnel | |
|[**viewAbsencePersonnel3**](#viewabsencepersonnel3) | **PUT** /api/absence/absencepersonnel | |
|[**viewAbsencePersonnel4**](#viewabsencepersonnel4) | **PATCH** /api/absence/absencepersonnel | |
|[**viewAbsencePersonnel5**](#viewabsencepersonnel5) | **DELETE** /api/absence/absencepersonnel | |
|[**viewAbsencePersonnel6**](#viewabsencepersonnel6) | **OPTIONS** /api/absence/absencepersonnel | |

# **deleteAbsencesPersonnel**
> AbsencesPersonnelDTO deleteAbsencesPersonnel(idRequest)


### Example

```typescript
import {
    AbsencesPersonnelControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesPersonnelControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.deleteAbsencesPersonnel(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**AbsencesPersonnelDTO**

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

# **findAbsencePersonnelsByAbsence**
> AbsencesPersonnelDTO findAbsencePersonnelsByAbsence(idRequest)


### Example

```typescript
import {
    AbsencesPersonnelControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesPersonnelControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.findAbsencePersonnelsByAbsence(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**AbsencesPersonnelDTO**

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

# **findAbsencePersonnelsByPersonnel**
> AbsencesPersonnelDTO findAbsencePersonnelsByPersonnel(idRequest)


### Example

```typescript
import {
    AbsencesPersonnelControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesPersonnelControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.findAbsencePersonnelsByPersonnel(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**AbsencesPersonnelDTO**

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

# **findAbsencesPersonnel**
> AbsencesPersonnelDTO findAbsencesPersonnel(idRequest)


### Example

```typescript
import {
    AbsencesPersonnelControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesPersonnelControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.findAbsencesPersonnel(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**AbsencesPersonnelDTO**

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

# **findAbsencesPersonnels**
> AbsencesPersonnelDTO findAbsencesPersonnels()


### Example

```typescript
import {
    AbsencesPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.findAbsencesPersonnels();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**AbsencesPersonnelDTO**

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

# **getUserListabsenceJSON**
> AbsencesPersonnelDTO getUserListabsenceJSON(paginationRequest)


### Example

```typescript
import {
    AbsencesPersonnelControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesPersonnelControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getUserListabsenceJSON(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**AbsencesPersonnelDTO**

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

# **saveAbsencesPersonnel**
> AbsencesPersonnelDTO saveAbsencesPersonnel(absencesPersonnelRequest)


### Example

```typescript
import {
    AbsencesPersonnelControllerApi,
    Configuration,
    AbsencesPersonnelRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesPersonnelControllerApi(configuration);

let absencesPersonnelRequest: AbsencesPersonnelRequest; //

const { status, data } = await apiInstance.saveAbsencesPersonnel(
    absencesPersonnelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **absencesPersonnelRequest** | **AbsencesPersonnelRequest**|  | |


### Return type

**AbsencesPersonnelDTO**

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

# **viewAbsencePersonnel**
> string viewAbsencePersonnel()


### Example

```typescript
import {
    AbsencesPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.viewAbsencePersonnel();
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

# **viewAbsencePersonnel1**
> string viewAbsencePersonnel1()


### Example

```typescript
import {
    AbsencesPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.viewAbsencePersonnel1();
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

# **viewAbsencePersonnel2**
> string viewAbsencePersonnel2()


### Example

```typescript
import {
    AbsencesPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.viewAbsencePersonnel2();
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

# **viewAbsencePersonnel3**
> string viewAbsencePersonnel3()


### Example

```typescript
import {
    AbsencesPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.viewAbsencePersonnel3();
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

# **viewAbsencePersonnel4**
> string viewAbsencePersonnel4()


### Example

```typescript
import {
    AbsencesPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.viewAbsencePersonnel4();
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

# **viewAbsencePersonnel5**
> string viewAbsencePersonnel5()


### Example

```typescript
import {
    AbsencesPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.viewAbsencePersonnel5();
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

# **viewAbsencePersonnel6**
> string viewAbsencePersonnel6()


### Example

```typescript
import {
    AbsencesPersonnelControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AbsencesPersonnelControllerApi(configuration);

const { status, data } = await apiInstance.viewAbsencePersonnel6();
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

