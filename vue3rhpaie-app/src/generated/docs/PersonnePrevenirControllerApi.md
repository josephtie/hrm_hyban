# PersonnePrevenirControllerApi

All URIs are relative to *http://localhost:7200*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**deletePersonnePrevenir**](#deletepersonneprevenir) | **POST** /api/personnel/supprimerpersonneprevenir | |
|[**disablePersonnePrevenir**](#disablepersonneprevenir) | **POST** /api/personnel/desactiverpersonneprevenir | |
|[**enablePersonnePrevenir**](#enablepersonneprevenir) | **POST** /api/personnel/activerpersonneprevenir | |
|[**findEnfant**](#findenfant) | **POST** /api/personnel/trouverpersonneprevenir | |
|[**findPersonnePrevenirs**](#findpersonneprevenirs) | **POST** /api/personnel/listerpersonnesprevenir | |
|[**findPersonnePrevenirsByPersonnel**](#findpersonneprevenirsbypersonnel) | **POST** /api/personnel/listerpersonnesprevenirparpersonnel | |
|[**getPersonnePrevenirListJSON**](#getpersonneprevenirlistjson) | **POST** /api/personnel/paginerpersonnesprevenir | |
|[**savePersonnePrevenir**](#savepersonneprevenir) | **POST** /api/personnel/enregistrerpersonneprevenir | |
|[**viewEnfant1**](#viewenfant1) | **GET** /api/personnel/personnesprevenir | |
|[**viewEnfant2**](#viewenfant2) | **HEAD** /api/personnel/personnesprevenir | |
|[**viewEnfant3**](#viewenfant3) | **POST** /api/personnel/personnesprevenir | |
|[**viewEnfant4**](#viewenfant4) | **PUT** /api/personnel/personnesprevenir | |
|[**viewEnfant5**](#viewenfant5) | **PATCH** /api/personnel/personnesprevenir | |
|[**viewEnfant6**](#viewenfant6) | **DELETE** /api/personnel/personnesprevenir | |
|[**viewEnfant7**](#viewenfant7) | **OPTIONS** /api/personnel/personnesprevenir | |

# **deletePersonnePrevenir**
> PersonnePrevenirDTO deletePersonnePrevenir(idRequest)


### Example

```typescript
import {
    PersonnePrevenirControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnePrevenirControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.deletePersonnePrevenir(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**PersonnePrevenirDTO**

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

# **disablePersonnePrevenir**
> PersonnePrevenirDTO disablePersonnePrevenir(idRequest)


### Example

```typescript
import {
    PersonnePrevenirControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnePrevenirControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.disablePersonnePrevenir(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**PersonnePrevenirDTO**

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

# **enablePersonnePrevenir**
> PersonnePrevenirDTO enablePersonnePrevenir(idRequest)


### Example

```typescript
import {
    PersonnePrevenirControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnePrevenirControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.enablePersonnePrevenir(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**PersonnePrevenirDTO**

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

# **findEnfant**
> PersonnePrevenirDTO findEnfant(idRequest)


### Example

```typescript
import {
    PersonnePrevenirControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnePrevenirControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.findEnfant(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**PersonnePrevenirDTO**

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

# **findPersonnePrevenirs**
> PersonnePrevenirDTO findPersonnePrevenirs()


### Example

```typescript
import {
    PersonnePrevenirControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnePrevenirControllerApi(configuration);

const { status, data } = await apiInstance.findPersonnePrevenirs();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**PersonnePrevenirDTO**

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

# **findPersonnePrevenirsByPersonnel**
> PersonnePrevenirDTO findPersonnePrevenirsByPersonnel(idRequest)


### Example

```typescript
import {
    PersonnePrevenirControllerApi,
    Configuration,
    IdRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnePrevenirControllerApi(configuration);

let idRequest: IdRequest; //

const { status, data } = await apiInstance.findPersonnePrevenirsByPersonnel(
    idRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **idRequest** | **IdRequest**|  | |


### Return type

**PersonnePrevenirDTO**

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

# **getPersonnePrevenirListJSON**
> PersonnePrevenirDTO getPersonnePrevenirListJSON(paginationRequest)


### Example

```typescript
import {
    PersonnePrevenirControllerApi,
    Configuration,
    PaginationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnePrevenirControllerApi(configuration);

let paginationRequest: PaginationRequest; //

const { status, data } = await apiInstance.getPersonnePrevenirListJSON(
    paginationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **paginationRequest** | **PaginationRequest**|  | |


### Return type

**PersonnePrevenirDTO**

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

# **savePersonnePrevenir**
> PersonnePrevenirDTO savePersonnePrevenir(enfantRequest)


### Example

```typescript
import {
    PersonnePrevenirControllerApi,
    Configuration,
    EnfantRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnePrevenirControllerApi(configuration);

let enfantRequest: EnfantRequest; //

const { status, data } = await apiInstance.savePersonnePrevenir(
    enfantRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **enfantRequest** | **EnfantRequest**|  | |


### Return type

**PersonnePrevenirDTO**

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

# **viewEnfant1**
> object viewEnfant1()


### Example

```typescript
import {
    PersonnePrevenirControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnePrevenirControllerApi(configuration);

const { status, data } = await apiInstance.viewEnfant1();
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

# **viewEnfant2**
> object viewEnfant2()


### Example

```typescript
import {
    PersonnePrevenirControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnePrevenirControllerApi(configuration);

const { status, data } = await apiInstance.viewEnfant2();
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

# **viewEnfant3**
> object viewEnfant3()


### Example

```typescript
import {
    PersonnePrevenirControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnePrevenirControllerApi(configuration);

const { status, data } = await apiInstance.viewEnfant3();
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

# **viewEnfant4**
> object viewEnfant4()


### Example

```typescript
import {
    PersonnePrevenirControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnePrevenirControllerApi(configuration);

const { status, data } = await apiInstance.viewEnfant4();
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

# **viewEnfant5**
> object viewEnfant5()


### Example

```typescript
import {
    PersonnePrevenirControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnePrevenirControllerApi(configuration);

const { status, data } = await apiInstance.viewEnfant5();
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

# **viewEnfant6**
> object viewEnfant6()


### Example

```typescript
import {
    PersonnePrevenirControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnePrevenirControllerApi(configuration);

const { status, data } = await apiInstance.viewEnfant6();
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

# **viewEnfant7**
> object viewEnfant7()


### Example

```typescript
import {
    PersonnePrevenirControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new PersonnePrevenirControllerApi(configuration);

const { status, data } = await apiInstance.viewEnfant7();
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

