<?php

namespace App\Transformers;

use App\Models\Country;

class CountryTransformer extends Transformer
{
    /**
     * A Fractal transformer.
     *
     * @param Country $country
     * @return array
     */
    public function transform(Country $country)
    {
        return [
            'id' => $country->id,
            'dial_code' => $country->dial_code,
            'name' => $country->name,
            'code' => $country->code,
            'flag'=>$country->flag,
            'active' => (bool)$country->active,
        ];
    }
}
