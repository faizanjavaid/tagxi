<?php

namespace App\Http\Requests\Admin\ServiceLocation;

use Illuminate\Foundation\Http\FormRequest;

class UpdateServiceLocationRequest extends FormRequest
{
    /**
     * Determine if the user is authorized to make this request.
     *
     * @return bool
     */
    public function authorize()
    {
        return true;
    }

    /**
     * Get the validation rules that apply to the request.
     *
     * @return array
     */
    public function rules()
    {
        return [
            'name' => 'required|min:5|max:50|unique:service_locations,name,'.$this->service_location->id.',id,deleted_at,NULL',
            'currency_code' => 'required',
            'currency_symbol' => 'required',
            'timezone' => 'required',
            'country' => 'required'
        ];
    }
}
