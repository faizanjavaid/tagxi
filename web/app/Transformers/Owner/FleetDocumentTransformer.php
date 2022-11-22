<?php

namespace App\Transformers\Owner;

use App\Transformers\Transformer;
use App\Models\Admin\FleetDocument;
use App\Base\Constants\Masters\DriverDocumentStatus;
use App\Base\Constants\Masters\DriverDocumentStatusString;

class FleetDocumentTransformer extends Transformer
{
    /**
     * Resources that can be included if requested.
     *
     * @var array
     */
    protected $availableIncludes = [

    ];

    /**
     * A Fractal transformer.
     *
     * @return array
     */
    public function transform(FleetDocument $document)
    {
        $params = [
            'id' => $document->id,
            'fleet_id' => $document->fleet_id,
            'document_id' => $document->document_id,
            'document_name' => $document->name,
            'document' => $document->image,
            'identify_number' => $document->identify_number,
            'expiry_date' => $document->expiry_date,
            'comment'=>$document->comment,
            'document_status'=>$document->document_status
            // 'identify_number_key' => $document->identify_number_key,
        ];

        foreach (DriverDocumentStatus::DocumentStatus() as $key=> $document_string) {
            if ($document->document_status==$key) {
                $params['document_status_string'] = $document_string;
            }
        }
        return $params;
    }
}
