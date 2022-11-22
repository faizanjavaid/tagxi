<?php

use Illuminate\Database\Seeder;
use App\Models\Admin\CancellationReason;

class CancellationReasonSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    protected $cancelation_reason = [
       [ 'user_type' => 'user',
        'payment_type' => 'free',
        'arrival_status' => 'before_arravial',
        'reason' => 'Waiting for driver long Time',
        'active' => 1,
        'company_key' => null,],
       [ 'user_type' => 'user',
        'payment_type' => 'free',
        'arrival_status' => 'after_arravial',
        'reason' => 'Driver Drinked',
        'active' => 1,
        'company_key' => null,],
       [ 'user_type' => 'user',
        'payment_type' => 'compensate',
        'arrival_status' => 'after_arravial',
        'reason' => 'Driver Too Late',
        'active' => 1,
        'company_key' => null,],
       [ 'user_type' => 'user',
        'payment_type' => 'compensate',
        'arrival_status' => 'before_arravial',
        'reason' => 'Self Cancelation',
        'active' => 1,
        'company_key' => null,],

        [ 'user_type' => 'driver',
        'payment_type' => 'free',
        'arrival_status' => 'before_arravial',
        'reason' => 'User Cancelled',
        'active' => 1,
        'company_key' => null,],
        [ 'user_type' => 'driver',
        'payment_type' => 'free',
        'arrival_status' => 'after_arravial',
        'reason' => 'User Not Receive Calls',
        'active' => 1,
        'company_key' => null,],
        [ 'user_type' => 'driver',
        'payment_type' => 'compensate',
        'arrival_status' => 'before_arravial',
        'reason' => 'Self Cancelation',
        'active' => 1,
        'company_key' => null,],
        [ 'user_type' => 'driver',
        'payment_type' => 'compensate',
        'arrival_status' => 'after_arravial',
        'reason' => 'User Too Late',
        'active' => 1,
        'company_key' => null,]
    ];


    public function run()
    {


      $created_params = $this->cancelation_reason;

      foreach ($created_params as $reason) 
      {
      
        CancellationReason::create($reason);
    
      }

    }
}
